package cn.iotzone.mindlocal.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iotzone.mindlocal.R;
import cn.iotzone.mindlocal.adapter.DeviceListAdapter;
import cn.iotzone.mindlocal.app.MainApplication;
import cn.iotzone.mindlocal.base.BaseActivity;
import cn.iotzone.mindlocal.db.DBDevice;
import cn.iotzone.mindlocal.udp.MQTTMessage;
import cn.iotzone.mindlocal.udp.RxBus;
import cn.iotzone.mindlocal.udp.UDPUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements DeviceListAdapter.IClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final int REQUEST_ADD = 1;
    @BindView(R.id.tool_title)
    TextView mToolTitle;
    @BindView(R.id.tool_left)
    ImageView mToolLeft;
    @BindView(R.id.tool_right)
    ImageView mToolRight;
    @BindView(R.id.current_ip)
    TextView mCurrentIp;
    @BindView(R.id.device_list)
    RecyclerView mDeviceList;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    private DeviceListAdapter mDeviceListAdapter;
    private List<DBDevice> mDevices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();

        Disposable mStateDisposable = RxBus.get().toObservable(MQTTMessage.class)
                .subscribeOn(Schedulers.io())
                .compose(this.<MQTTMessage>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MQTTMessage>() {
                    @Override
                    public void accept(MQTTMessage mqttMessage) throws Exception {
                        for (int i = 0; i < mDevices.size(); i++) {
                            if (mDevices.get(i).getDeviceIp().equals(mqttMessage.getTopic())){
                                DBDevice device = mDevices.get(i);
                                device.setState(true);
                                mDevices.set(i,device);
                                mDeviceListAdapter.setData(mDevices);
                            }
                        }
                    }
                });
        getDisposables().add(mStateDisposable);
    }

    @Override
    protected void initViews() {
        mToolTitle.setText(getString(R.string.home));
        Glide.with(this).load(R.mipmap.icon_add).into(mToolRight);
        mCurrentIp.setText(getIP());

        mDeviceList.setLayoutManager(new LinearLayoutManager(this));
        mDeviceListAdapter = new DeviceListAdapter();
        mDeviceList.setAdapter(mDeviceListAdapter);
        mDeviceListAdapter.setClickListener(this);

        mSwipeRefresh.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDevice();
    }

    private void getDevice() {
        String ip = getIP();
        mCurrentIp.setText(ip);
        List<DBDevice> list = MainApplication.getDaoInstant().getDBDeviceDao().queryBuilder().list();
        if (list != null) {
            mDevices = list;
            mDeviceListAdapter.setData(mDevices);
            for (int i = 0; i < list.size(); i++) {
                UDPUtils.sendUdp(mDevices.get(i).getDeviceIp(),"state=?");
            }
        }
        mSwipeRefresh.setRefreshing(false);
    }

    public String getIP() {
        WifiManager wifiManager = (WifiManager) MainApplication.get().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                (ipAddress >> 24 & 0xFF);
    }

    @Override
    public void doClick(int paramInt, DBDevice paramBeanDevice) {
        startActivity(new Intent(this, ControlActivity.class).putExtra("ip", paramBeanDevice.getDeviceIp()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_ADD) {
            getDevice();
        }

    }

    @OnClick(R.id.tool_right)
    public void onViewClicked() {
        startActivityForResult(new Intent(this, DeviceAddActivity.class), REQUEST_ADD);
    }

    @Override
    public void onRefresh() {
        mSwipeRefresh.setRefreshing(true);
        getDevice();
    }
}
