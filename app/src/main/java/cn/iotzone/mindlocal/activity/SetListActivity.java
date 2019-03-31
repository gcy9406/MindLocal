package cn.iotzone.mindlocal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.appcompat.app.AlertDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iotzone.mindlocal.R;
import cn.iotzone.mindlocal.app.MainApplication;
import cn.iotzone.mindlocal.base.BaseActivity;
import cn.iotzone.mindlocal.db.DBDevice;
import cn.iotzone.mindlocal.db.DBDeviceDao;

public class SetListActivity extends BaseActivity {

    @BindView(R.id.tool_title)
    TextView mToolTitle;
    @BindView(R.id.tool_left)
    ImageView mToolLeft;
    @BindView(R.id.tool_right)
    ImageView mToolRight;
    @BindView(R.id.set_name)
    LinearLayout mSetName;
    @BindView(R.id.set_output)
    LinearLayout mSetOutput;
    @BindView(R.id.set_input)
    LinearLayout mSetInput;
    private String mIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_list);
        ButterKnife.bind(this);
        initViews();
        mIp = getIntent().getStringExtra("ip");
    }

    @Override
    protected void initViews() {
        mToolLeft.setImageResource(R.mipmap.icon_back);
        mToolTitle.setText(getString(R.string.set));
    }

    @OnClick({R.id.tool_left, R.id.set_name, R.id.set_output, R.id.set_input,R.id.set_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_left:
                finish();
                break;
            case R.id.set_name:
                startActivity(new Intent(this,SetNameActivity.class).putExtra("ip",mIp));
                break;
            case R.id.set_output:
                startActivity(new Intent(this,ModifyOutputActivity.class).putExtra("ip",mIp));
                break;
            case R.id.set_input:
                startActivity(new Intent(this,ModifyInputActivity.class).putExtra("ip",mIp));
                break;
            case R.id.set_del:
                new AlertDialog.Builder(this)
                        .setIcon(R.mipmap.logo_512_local)
                        .setTitle(getString(R.string.delete_device))
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                List<DBDevice> list = MainApplication.getDaoInstant().getDBDeviceDao().queryBuilder().where(DBDeviceDao.Properties.DeviceIp.eq(mIp)).list();
                                MainApplication.getDaoInstant().getDBDeviceDao().deleteInTx(list);
                                startActivity(new Intent(SetListActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            }
                        })
                        .create()
                        .show();
                break;
        }
    }
}
