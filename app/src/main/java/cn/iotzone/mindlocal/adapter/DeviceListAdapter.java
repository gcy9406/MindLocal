package cn.iotzone.mindlocal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.iotzone.mindlocal.R;
import cn.iotzone.mindlocal.bean.BeanDevice;
import cn.iotzone.mindlocal.db.DBDevice;
import de.hdodenhof.circleimageview.CircleImageView;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceListViewHolder> {
    private List<DBDevice> data = new ArrayList();
    private IClickListener mClickListener;

    public int getItemCount() {
        return this.data.size();
    }

    public void onBindViewHolder(@NonNull final DeviceListViewHolder holder, int paramInt) {
        final DBDevice localBeanDevice = data.get(paramInt);
        holder.mDeviceName.setText(localBeanDevice.getDeviceName());
        holder.mDeviceIp.setText(localBeanDevice.getDeviceIp());
        Glide
                .with(holder.mDeviceHead.getContext())
                .load(localBeanDevice.getDeviceHead())
                .apply(new RequestOptions().placeholder(R.mipmap.icon_device_default).error(R.mipmap.icon_device_default))
                .into(holder.mDeviceHead);
        if (localBeanDevice.getState()) {
            Glide
                    .with(holder.mDeviceState.getContext())
                    .load(R.mipmap.icon_state_on)
                    .apply(new RequestOptions().placeholder(R.mipmap.icon_state_on).error(R.mipmap.icon_state_on))
                    .into(holder.mDeviceState);
        } else {
            Glide
                    .with(holder.mDeviceState.getContext())
                    .load(R.mipmap.icon_state_off)
                    .apply(new RequestOptions().placeholder(R.mipmap.icon_state_off).error(R.mipmap.icon_state_off))
                    .into(holder.mDeviceState);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (DeviceListAdapter.this.mClickListener != null) {
                    int i = holder.getLayoutPosition();
                    DeviceListAdapter.this.mClickListener.doClick(i, localBeanDevice);
                }
            }
        });
    }

    @NonNull
    public DeviceListViewHolder onCreateViewHolder(@NonNull ViewGroup paramViewGroup, int paramInt) {
        return new DeviceListViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.adapter_device_list_item, paramViewGroup, false));
    }

    public void setClickListener(IClickListener paramIClickListener) {
        this.mClickListener = paramIClickListener;
    }

    public void setData(List<DBDevice> paramList) {
        this.data = paramList;
        notifyDataSetChanged();
    }

    class DeviceListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.device_head)
        CircleImageView mDeviceHead;
        @BindView(R.id.device_state)
        ImageView mDeviceState;
        @BindView(R.id.device_name)
        TextView mDeviceName;
        @BindView(R.id.device_ip)
        TextView mDeviceIp;

        public DeviceListViewHolder(View paramView) {
            super(paramView);
            ButterKnife.bind(this, paramView);
        }
    }

    public interface IClickListener {
        void doClick(int paramInt, DBDevice paramBeanDevice);
    }
}