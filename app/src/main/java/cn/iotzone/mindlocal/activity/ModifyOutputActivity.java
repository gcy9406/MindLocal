package cn.iotzone.mindlocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iotzone.mindlocal.R;
import cn.iotzone.mindlocal.adapter.OutputInfoAdapter;
import cn.iotzone.mindlocal.app.MainApplication;
import cn.iotzone.mindlocal.base.BaseActivity;
import cn.iotzone.mindlocal.bean.BeanOutput;
import cn.iotzone.mindlocal.db.DBDevice;
import cn.iotzone.mindlocal.db.DBDeviceDao;

public class ModifyOutputActivity extends BaseActivity implements OutputInfoAdapter.IClickListener {

    private static final int REQUEST_OUTPUT = 1;
    @BindView(R.id.tool_title)
    TextView mToolTitle;
    @BindView(R.id.tool_left)
    ImageView mToolLeft;
    @BindView(R.id.tool_right)
    ImageView mToolRight;
    @BindView(R.id.relay_list)
    RecyclerView mRelayList;
    private OutputInfoAdapter mOutputInfoAdapter;
    private String mIp;
    private List<DBDevice> mDevices;
    private List<BeanOutput> mBeanOutputs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_output);
        ButterKnife.bind(this);
        mIp = getIntent().getStringExtra("ip");
        mDevices = MainApplication.getDaoInstant().getDBDeviceDao().queryBuilder().where(DBDeviceDao.Properties.DeviceIp.eq(mIp)).list();
        initViews();
    }

    @Override
    protected void initViews() {
        mToolLeft.setImageResource(R.mipmap.icon_back);
        mToolRight.setImageResource(R.mipmap.icon_finish);
        mToolTitle.setText(getString(R.string.set_output_info));
        mOutputInfoAdapter = new OutputInfoAdapter();
        mRelayList.setLayoutManager(new LinearLayoutManager(this));
        mRelayList.setAdapter(mOutputInfoAdapter);
        mOutputInfoAdapter.setClickListener(this);
        if (mDevices != null && mDevices.size() > 0) {
            mBeanOutputs = MainApplication.getGson().fromJson(mDevices.get(0).getDeviceOutput(),new TypeToken<List<BeanOutput>>(){}.getType());
            mOutputInfoAdapter.setData(mBeanOutputs);
        }else {
            Toast.makeText(this, getString(R.string.device_error), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.tool_left, R.id.tool_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_left:
                finish();
                break;
            case R.id.tool_right:
                String output = MainApplication.getGson().toJson(mBeanOutputs);
                mDevices.get(0).setDeviceOutput(output);
                MainApplication.getDaoInstant().getDBDeviceDao().updateInTx(mDevices);
                startActivity(new Intent(this,ControlActivity.class).putExtra("ip",mIp).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;
        }
    }

    @Override
    public void doClick(int pos, BeanOutput beanOutput) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos",pos);
        bundle.putSerializable("output",beanOutput);
        startActivityForResult(new Intent(this,ModityOutputOneActivity.class).putExtras(bundle),REQUEST_OUTPUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_OUTPUT){
            Bundle dataExtras = data.getExtras();
            int pos = dataExtras.getInt("pos");
            BeanOutput beanOutput = (BeanOutput) dataExtras.getSerializable("output");
            mBeanOutputs.set(pos,beanOutput);
            mOutputInfoAdapter.setData(mBeanOutputs);
        }
    }
}
