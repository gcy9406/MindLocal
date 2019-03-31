package cn.iotzone.mindlocal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class OutputAdapter extends RecyclerView.Adapter<OutputAdapter.OutputViewHolder> {
    private List<BeanOutput> data = new ArrayList<>();

    public void setData(List<BeanOutput> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OutputViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_output_list_item, parent, false);
        return new OutputViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OutputViewHolder holder, int position) {
        BeanOutput beanOutput = data.get(position);
        holder.mOutputName.setText(beanOutput.getName());
        Glide.with(holder.mOutputHead.getContext())
                .load(beanOutput.getHead())
                .apply(new RequestOptions().error(R.mipmap.icon_relay_default).placeholder(R.mipmap.icon_relay_default))
                .into(holder.mOutputHead);
        if (beanOutput.isState()) {
            Glide.with(holder.mOutputState.getContext())
                    .load(R.mipmap.icon_state_on)
                    .into(holder.mOutputState);
        } else {
            Glide.with(holder.mOutputState.getContext())
                    .load(R.mipmap.icon_state_off)
                    .into(holder.mOutputState);
        }
        setMode(holder,beanOutput.getMode());

    }

    public void setMode(final OutputViewHolder holder, int mode) {
        holder.mLayoutOnoff.setVisibility(View.GONE);
        holder.mLayoutPulse.setVisibility(View.GONE);
        holder.mLayoutTurn.setVisibility(View.GONE);
        holder.mLayoutLock.setVisibility(View.GONE);
        switch (mode) {
            case 0:
                holder.mLayoutOnoff.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.mLayoutPulse.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.mLayoutTurn.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.mLayoutLock.setVisibility(View.VISIBLE);
                break;
            default:
                holder.mLayoutOnoff.setVisibility(View.VISIBLE);
                break;
        }

        holder.mRelayOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.doClick(holder.getLayoutPosition(),1);
                }
            }
        });
        holder.mRelayOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.doClick(holder.getLayoutPosition(),0);
                }
            }
        });
        holder.mRelayPulse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.doClick(holder.getLayoutPosition(),2);
                }
            }
        });
        holder.mRelayTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.doClick(holder.getLayoutPosition(),3);
                }
            }
        });
        holder.mRelayLockOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.doClick(holder.getLayoutPosition(),4);
                }
            }
        });
        holder.mRelayLockOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.doClick(holder.getLayoutPosition(),0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class OutputViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.output_head)
        CircleImageView mOutputHead;
        @BindView(R.id.output_state)
        ImageView mOutputState;
        @BindView(R.id.output_name)
        TextView mOutputName;
        @BindView(R.id.relay_on)
        Button mRelayOn;
        @BindView(R.id.relay_off)
        Button mRelayOff;
        @BindView(R.id.layout_onoff)
        LinearLayout mLayoutOnoff;
        @BindView(R.id.relay_pulse)
        Button mRelayPulse;
        @BindView(R.id.layout_pulse)
        LinearLayout mLayoutPulse;
        @BindView(R.id.relay_turn)
        Button mRelayTurn;
        @BindView(R.id.layout_turn)
        LinearLayout mLayoutTurn;
        @BindView(R.id.relay_lock_on)
        Button mRelayLockOn;
        @BindView(R.id.relay_lock_off)
        Button mRelayLockOff;
        @BindView(R.id.layout_lock)
        LinearLayout mLayoutLock;

        public OutputViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface IClickListener {
        void doClick(int pos, int cmd);
    }

    private IClickListener mClickListener;

    public void setClickListener(IClickListener clickListener) {
        mClickListener = clickListener;
    }
}
