package cn.iotzone.mindlocal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cn.iotzone.mindlocal.bean.BeanOutput;
import de.hdodenhof.circleimageview.CircleImageView;

public class OutputInfoAdapter extends RecyclerView.Adapter<OutputInfoAdapter.OutputInfoViewHolder> {
    private List<BeanOutput> data = new ArrayList<>();

    public void setData(List<BeanOutput> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OutputInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_output_info_item, parent, false);
        return new OutputInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final OutputInfoViewHolder holder, int position) {
        final BeanOutput beanOutput = data.get(position);
        Glide
                .with(holder.mRelayHead.getContext())
                .load(beanOutput.getHead())
                .apply(new RequestOptions().error(R.mipmap.icon_relay_default).placeholder(R.mipmap.icon_relay_default))
                .into(holder.mRelayHead);
        holder.mRelayName.setText(beanOutput.getName());
        String[] modes=holder.itemView.getResources().getStringArray(R.array.mode);
        holder.mRelayMode.setText(modes[beanOutput.getMode()]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.doClick(holder.getLayoutPosition(),beanOutput);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class OutputInfoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.relay_head)
        CircleImageView mRelayHead;
        @BindView(R.id.relay_name)
        TextView mRelayName;
        @BindView(R.id.relay_mode)
        TextView mRelayMode;
        public OutputInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface IClickListener {
        void doClick(int pos,BeanOutput beanOutput);
    }

    private IClickListener mClickListener;

    public void setClickListener(IClickListener clickListener) {
        mClickListener = clickListener;
    }
}
