package cn.iotzone.mindlocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iotzone.mindlocal.R;
import cn.iotzone.mindlocal.adapter.InputInfoAdapter;
import cn.iotzone.mindlocal.app.MainApplication;
import cn.iotzone.mindlocal.base.BaseActivity;
import cn.iotzone.mindlocal.bean.BeanIntput;
import cn.iotzone.mindlocal.bean.BeanOutput;
import cn.iotzone.mindlocal.db.DBDevice;
import cn.iotzone.mindlocal.db.DBDeviceDao;

public class ModifyInputActivity extends BaseActivity implements InputInfoAdapter.IClickListener {

    private static final int REQUEST_INPUT = 1;
    @BindView(R.id.tool_title)
    TextView mToolTitle;
    @BindView(R.id.tool_left)
    ImageView mToolLeft;
    @BindView(R.id.tool_right)
    ImageView mToolRight;
    @BindView(R.id.input_list)
    RecyclerView mInputList;
    private InputInfoAdapter mInputInfoAdapter;
    private String mIp;
    private List<DBDevice> mDevices;
    private List<BeanIntput> mBeanInputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_input);
        ButterKnife.bind(this);
        mIp = getIntent().getStringExtra("ip");
        mDevices = MainApplication.getDaoInstant().getDBDeviceDao().queryBuilder().where(DBDeviceDao.Properties.DeviceIp.eq(mIp)).list();

        initViews();
    }

    @Override
    protected void initViews() {
        mToolLeft.setImageResource(R.mipmap.icon_back);
        mToolTitle.setText(getString(R.string.set_input_info));
        mToolTitle.setTextSize(18);
        mToolRight.setImageResource(R.mipmap.icon_finish);
        mInputList.setLayoutManager(new LinearLayoutManager(this));
        mInputInfoAdapter = new InputInfoAdapter();
        mInputList.setAdapter(mInputInfoAdapter);
        mInputInfoAdapter.setClickListener(this);
        if (mDevices != null && mDevices.size() > 0) {
            mBeanInputs = MainApplication.getGson().fromJson(mDevices.get(0).getDeviceInput(),new TypeToken<List<BeanIntput>>(){}.getType());
            mInputInfoAdapter.setData(mBeanInputs);
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
                String input = MainApplication.getGson().toJson(mBeanInputs);
                mDevices.get(0).setDeviceInput(input);
                MainApplication.getDaoInstant().getDBDeviceDao().updateInTx(mDevices);
                startActivity(new Intent(this,ControlActivity.class).putExtra("ip",mIp).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;
        }
    }

    @Override
    public void doClick(int pos, BeanIntput beanIntput) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos",pos);
        bundle.putSerializable("input",beanIntput);
        startActivityForResult(new Intent(this,ModifyInputOneActivity.class).putExtras(bundle),REQUEST_INPUT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_INPUT){
            Bundle dataExtras = data.getExtras();
            int pos = dataExtras.getInt("pos");
            BeanIntput beanInput = (BeanIntput) dataExtras.getSerializable("input");
            mBeanInputs.set(pos,beanInput);
            mInputInfoAdapter.setData(mBeanInputs);
        }
    }
}
