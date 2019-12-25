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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iotzone.mindlocal.R;
import cn.iotzone.mindlocal.base.BaseActivity;
import cn.iotzone.mindlocal.bean.BeanIntput;
import cn.iotzone.mindlocal.bean.BeanOutput;
import de.hdodenhof.circleimageview.CircleImageView;
import me.iwf.photopicker.PhotoPicker;

public class ModifyInputOneActivity extends BaseActivity {

    @BindView(R.id.tool_title)
    TextView mToolTitle;
    @BindView(R.id.tool_left)
    ImageView mToolLeft;
    @BindView(R.id.tool_right)
    ImageView mToolRight;
    @BindView(R.id.name)
    EditText mName;
    @BindView(R.id.head)
    CircleImageView mHead;
    private BeanIntput mBeanInput;
    private int mPos;
    private String mHeadPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_input_one);
        ButterKnife.bind(this);
        mBeanInput = (BeanIntput) getIntent().getSerializableExtra("input");
        mPos = getIntent().getIntExtra("pos",0);
        initViews();
    }

    @Override
    protected void initViews() {
        mToolLeft.setImageResource(R.mipmap.icon_back);
        mToolTitle.setText(getString(R.string.set_input_info));
        mToolTitle.setTextSize(18);
        mToolRight.setImageResource(R.mipmap.icon_finish);
        mName.setText(mBeanInput.getName());
        mName.setSelection(mBeanInput.getName().length());
        mHeadPath = mBeanInput.getHead();
        Glide
                .with(this)
                .load(mHeadPath)
                .apply(new RequestOptions()
                        .error(R.mipmap.icon_input_default)
                        .placeholder(R.mipmap.icon_input_default))
                .into(mHead);
    }

    @OnClick({R.id.tool_left, R.id.tool_right, R.id.head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_left:
                finish();
                break;
            case R.id.tool_right:
                String name = mName.getText().toString();
                if ("".equals(name)){
                    Toast.makeText(this, getString(R.string.name_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                mBeanInput.setName(name);
                Bundle bundle = new Bundle();
                bundle.putInt("pos",mPos);
                bundle.putSerializable("input",mBeanInput);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
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
                    mBeanInput.setHead(mHeadPath);
                    Glide
                            .with(this)
                            .load(mHeadPath)
                            .apply(new RequestOptions()
                                    .error(R.mipmap.icon_relay_default)
                                    .placeholder(R.mipmap.icon_relay_default))
                            .into(mHead);
                }
            }
        }
    }
}
