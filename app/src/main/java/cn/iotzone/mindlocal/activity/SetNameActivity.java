package cn.iotzone.mindlocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iotzone.mindlocal.R;
import cn.iotzone.mindlocal.app.MainApplication;
import cn.iotzone.mindlocal.base.BaseActivity;
import cn.iotzone.mindlocal.db.DBDevice;
import cn.iotzone.mindlocal.db.DBDeviceDao;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPicker;

public class SetNameActivity extends BaseActivity {

    @BindView(R.id.tool_title)
    TextView mToolTitle;
    @BindView(R.id.tool_left)
    ImageView mToolLeft;
    @BindView(R.id.tool_right)
    ImageView mToolRight;
    @BindView(R.id.device_name)
    EditText mDeviceName;
    @BindView(R.id.head)
    CircleImageView mHead;
    private String mIp;
    private List<DBDevice> mDevices;
    private String mHeadPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);
        ButterKnife.bind(this);
        mIp = getIntent().getStringExtra("ip");
        mDevices = MainApplication.getDaoInstant().getDBDeviceDao().queryBuilder().where(DBDeviceDao.Properties.DeviceIp.eq(mIp)).list();
        initViews();
    }

    @Override
    protected void initViews() {
        mToolLeft.setImageResource(R.mipmap.icon_back);
        mToolRight.setImageResource(R.mipmap.icon_finish);
        mToolTitle.setText(getString(R.string.device_info));
        mToolTitle.setTextSize(18);
        if (mDevices != null && mDevices.size() > 0) {
            mDeviceName.setText(mDevices.get(0).getDeviceName());
            mDeviceName.setSelection(mDevices.get(0).getDeviceName().length());
            mHeadPath = mDevices.get(0).getDeviceHead();
            Glide
                    .with(this)
                    .load(mHeadPath)
                    .apply(new RequestOptions().placeholder(R.mipmap.icon_device_default).error(R.mipmap.icon_device_default))
                    .into(mHead);
        }
    }

    @OnClick({R.id.tool_left, R.id.tool_right,R.id.head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_left:
                finish();
                break;
            case R.id.head:
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(this, PhotoPicker.REQUEST_CODE);
                break;
            case R.id.tool_right:
                if ("".equals(mDeviceName.getText().toString())) {
                    Toast.makeText(this, getString(R.string.name_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                DBDevice device = mDevices.get(0);
                device.setDeviceName(mDeviceName.getText().toString());
                device.setDeviceHead(mHeadPath);
                MainApplication.getDaoInstant().getDBDeviceDao().update(device);
                startActivity(new Intent(this, ControlActivity.class).putExtra("ip", mIp).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                if (photos != null && photos.size() > 0) {
                    mHeadPath = photos.get(0);
                    Glide
                            .with(this)
                            .load(mHeadPath)
                            .apply(new RequestOptions()
                                    .error(R.mipmap.icon_device_default)
                                    .placeholder(R.mipmap.icon_device_default))
                            .into(mHead);
                }
            }
        }
    }
}
