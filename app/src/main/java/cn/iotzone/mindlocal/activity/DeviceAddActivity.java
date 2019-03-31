package cn.iotzone.mindlocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import cn.iotzone.mindlocal.bean.BeanIntput;
import cn.iotzone.mindlocal.bean.BeanOutput;
import cn.iotzone.mindlocal.db.DBDevice;
import cn.iotzone.mindlocal.db.DBDeviceDao;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPicker;

public class DeviceAddActivity extends BaseActivity {

    @BindView(R.id.tool_title)
    TextView mToolTitle;
    @BindView(R.id.tool_left)
    ImageView mToolLeft;
    @BindView(R.id.tool_right)
    ImageView mToolRight;
    @BindView(R.id.add_ip)
    EditText mAddIp;
    @BindView(R.id.add_name)
    EditText mAddName;
    @BindView(R.id.add)
    Button mAdd;
    @BindView(R.id.add_image)
    CircleImageView mAddImage;
    private String mHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_add);
        ButterKnife.bind(this);
        initViews();
    }

    @Override
    protected void initViews() {
        mToolTitle.setText(getString(R.string.add_device));
        mToolLeft.setImageResource(R.mipmap.icon_back);
    }

    @OnClick({R.id.tool_left, R.id.add_image, R.id.add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_left:
                finish();
                break;
            case R.id.add_image:
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(this, PhotoPicker.REQUEST_CODE);
                break;
            case R.id.add:
                String name = mAddName.getText().toString();
                String ip = mAddIp.getText().toString();
                if (name.equals("")){
                    Toast.makeText(this, getString(R.string.name_empty), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (ip.split("\\.").length != 4){
                    Toast.makeText(this, getString(R.string.ip_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<DBDevice> devices = MainApplication.getDaoInstant().getDBDeviceDao().queryBuilder().where(DBDeviceDao.Properties.DeviceIp.eq(ip)).list();
                if (devices != null && devices.size() > 0){
                    Toast.makeText(this, getString(R.string.device_exist), Toast.LENGTH_SHORT).show();
                }else {
                    DBDevice device = new DBDevice();
                    device.setDeviceName(name);
                    device.setDeviceHead(mHead);
                    device.setDeviceIp(ip);
                    List<BeanOutput> beanOutputs = new ArrayList<>();
                    List<BeanIntput> beanInputs = new ArrayList<>();
                    for (int i = 0; i < 8; i++) {
                        BeanOutput beanOutput = new BeanOutput();
                        BeanIntput beanInput = new BeanIntput();
                        beanOutput.setName(getString(R.string.relay)+(i+1));
                        beanOutput.setMode(0);
                        beanOutput.setState(false);
                        beanOutput.setChannel(i);
                        beanOutputs.add(beanOutput);

                        beanInput.setName(getString(R.string.input)+(i+1));
                        beanInput.setState(false);
                        beanInput.setChannel(i);
                        beanInputs.add(beanInput);
                    }

                    device.setDeviceInput(MainApplication.getGson().toJson(beanInputs));
                    device.setDeviceOutput(MainApplication.getGson().toJson(beanOutputs));
                    MainApplication.getDaoInstant().getDBDeviceDao().insert(device);
                    setResult(RESULT_OK);
                    finish();
                }

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
                    mHead = photos.get(0);
                    Glide
                            .with(this)
                            .load(mHead)
                            .apply(new RequestOptions()
                                    .error(R.mipmap.icon_device_default)
                                    .placeholder(R.mipmap.icon_device_default))
                            .into(mAddImage);
                }
            }
        }
    }
}
