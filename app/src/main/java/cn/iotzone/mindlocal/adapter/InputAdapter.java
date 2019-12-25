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
import cn.iotzone.mindlocal.bean.BeanIntput;
import de.hdodenhof.circleimageview.CircleImageView;

public class InputAdapter extends RecyclerView.Adapter<InputAdapter.InputViewHolder> {
    private List<BeanIntput> data = new ArrayList<>();

    public void setData(List<BeanIntput> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InputViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_input_list_item, parent, false);
        return new InputViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InputViewHolder holder, int position) {
        BeanIntput beanInput = data.get(position);
        holder.mInputName.setText(beanInput.getName());
        Glide.with(holder.mInputName.getContext())
                .load(beanInput.getHead())
                .apply(new RequestOptions().error(R.mipmap.icon_input_default).placeholder(R.mipmap.icon_input_default))
                .into(holder.mInputHead);
        if (beanInput.isState()) {
            Glide.with(holder.mInputState.getContext())
                    .load(R.mipmap.icon_state_on)
                    .into(holder.mInputState);
        } else {
            Glide.with(holder.mInputState.getContext())
                    .load(R.mipmap.icon_state_off)
                    .into(holder.mInputState);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class InputViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.input_head)
        CircleImageView mInputHead;
        @BindView(R.id.input_state)
        ImageView mInputState;
        @BindView(R.id.input_name)
        TextView mInputName;
        public InputViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
