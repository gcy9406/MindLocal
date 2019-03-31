package cn.iotzone.mindlocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iotzone.mindlocal.R;
import cn.iotzone.mindlocal.adapter.InputAdapter;
import cn.iotzone.mindlocal.adapter.OutputAdapter;
import cn.iotzone.mindlocal.app.MainApplication;
import cn.iotzone.mindlocal.base.BaseActivity;
import cn.iotzone.mindlocal.bean.BeanCmd;
import cn.iotzone.mindlocal.bean.BeanIntput;
import cn.iotzone.mindlocal.bean.BeanOutput;
import cn.iotzone.mindlocal.db.DBDevice;
import cn.iotzone.mindlocal.db.DBDeviceDao;
import cn.iotzone.mindlocal.udp.MQTTMessage;
import cn.iotzone.mindlocal.udp.RxBus;
import cn.iotzone.mindlocal.udp.UDPUtils;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ControlActivity extends BaseActivity implements OutputAdapter.IClickListener {

    @BindView(R.id.tool_title)
    TextView mToolTitle;
    @BindView(R.id.tool_left)
    ImageView mToolLeft;
    @BindView(R.id.tool_right)
    ImageView mToolRight;
    @BindView(R.id.input_list)
    RecyclerView mInputList;
    @BindView(R.id.input_show)
    ImageView mInputShow;
    @BindView(R.id.output_list)
    RecyclerView mOutputList;
    private String mIp;
    private List<DBDevice> mDBDevices;
    private List<BeanIntput> mIntputs = new ArrayList<>();
    private List<BeanOutput> mOutputs = new ArrayList<>();
    private OutputAdapter mOutputAdapter;
    private InputAdapter mInputAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        ButterKnife.bind(this);
        initViews();
        mIp = getIntent().getStringExtra("ip");

        mDBDevices = MainApplication.getDaoInstant().getDBDeviceDao().queryBuilder().where(DBDeviceDao.Properties.DeviceIp.eq(mIp)).list();

        if (mDBDevices != null && mDBDevices.size() > 0) {
            mIntputs = MainApplication.getGson().fromJson(mDBDevices.get(0).getDeviceInput(),new TypeToken<List<BeanIntput>>(){}.getType());
            mOutputs = MainApplication.getGson().fromJson(mDBDevices.get(0).getDeviceOutput(),new TypeToken<List<BeanOutput>>(){}.getType());
            mOutputAdapter.setData(mOutputs);
            mInputAdapter.setData(mIntputs);
            mOutputAdapter.setClickListener(this);
        }else {
            Toast.makeText(this, getString(R.string.device_error), Toast.LENGTH_SHORT).show();
        }

        Disposable mStateDisposable = RxBus.get().toObservable(MQTTMessage.class)
                .subscribeOn(Schedulers.io())
                .compose(this.<MQTTMessage>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MQTTMessage>() {
                    @Override
                    public void accept(MQTTMessage mqttMessage) throws Exception {
                        if (mqttMessage.getTopic().equals(mIp)){
                            String mesg = mqttMessage.getMesg();
                            BeanCmd beanCmd = MainApplication.getGson().fromJson(mesg,BeanCmd.class);
                            for (int i = 0; i < 8; i++) {
                                BeanOutput beanOutput = mOutputs.get(i);
                                BeanIntput beanIntput = mIntputs.get(i);
                                if (beanCmd.getOutput().substring(i,i+1).equals("1")){
                                    beanOutput.setState(true);
                                }else {
                                    beanOutput.setState(false);
                                }
                                if (beanCmd.getInput().substring(i,i+1).equals("1")){
                                    beanIntput.setState(true);
                                }else {
                                    beanIntput.setState(false);
                                }
                                mOutputs.set(i,beanOutput);
                                mIntputs.set(i,beanIntput);
                            }
                            mOutputAdapter.setData(mOutputs);
                            mInputAdapter.setData(mIntputs);
                        }
                    }
                });
        getDisposables().add(mStateDisposable);

        requestDataByNet();
    }

    private void requestDataByNet() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        UDPUtils.sendUdp(mIp,"state=?");
                    }
                });
    }

    @Override
    protected void initViews() {
        mToolLeft.setImageResource(R.mipmap.icon_back);
        mToolTitle.setText(getString(R.string.control));
        mToolRight.setImageResource(R.mipmap.icon_set);
        mInputList.setVisibility(View.GONE);
        mInputList.setLayoutManager(new GridLayoutManager(this,4));

        mInputShow.setImageResource(R.mipmap.icon_down);

        mOutputList.setLayoutManager(new LinearLayoutManager(this));
        mOutputAdapter = new OutputAdapter();
        mInputAdapter = new InputAdapter();
        mOutputList.setAdapter(mOutputAdapter);
        mInputList.setAdapter(mInputAdapter);

    }

    @OnClick({R.id.tool_left, R.id.tool_right, R.id.input_show})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_left:
                finish();
                break;
            case R.id.tool_right:
                startActivity(new Intent(this,SetListActivity.class).putExtra("ip",mIp));
                break;
            case R.id.input_show:
                if (mInputList.getVisibility() == View.VISIBLE){
                    mInputList.setVisibility(View.GONE);
                    mInputShow.setImageResource(R.mipmap.icon_down);
                }else {
                    mInputList.setVisibility(View.VISIBLE);
                    mInputShow.setImageResource(R.mipmap.icon_up);
                }
                break;
        }
    }

    @Override
    public void doClick(int pos, int cmd) {
        UDPUtils.sendUdp(mIp,generateCmd(pos,cmd));
    }

    public String generateCmd(int pos,int cmd){
        StringBuilder sb = new StringBuilder("setr=");
        for (int i = 0; i < 8; i++) {
            if (i == pos){
                sb.append(cmd);
            }else {
                sb.append("x");
            }
        }
        return sb.toString();
    }
}
