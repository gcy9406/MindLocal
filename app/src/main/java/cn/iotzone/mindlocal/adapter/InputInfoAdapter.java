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
import cn.iotzone.mindlocal.bean.BeanIntput;
import de.hdodenhof.circleimageview.CircleImageView;

public class InputInfoAdapter extends RecyclerView.Adapter<InputInfoAdapter.InputInfoViewHolder> {
    private List<BeanIntput> data = new ArrayList<>();

    public void setData(List<BeanIntput> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InputInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_input_info_item, parent, false);
        return new InputInfoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final InputInfoViewHolder holder, int position) {
        final BeanIntput beanIntput = data.get(position);
        Glide
                .with(holder.mInputHead.getContext())
                .load(beanIntput.getHead())
                .apply(new RequestOptions().error(R.mipmap.icon_input_default).placeholder(R.mipmap.icon_input_default))
                .into(holder.mInputHead);
        holder.mInputName.setText(beanIntput.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.doClick(holder.getLayoutPosition(),beanIntput);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class InputInfoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.input_head)
        CircleImageView mInputHead;
        @BindView(R.id.input_name)
        TextView mInputName;
        public InputInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    private IClickListener mClickListener;

    public void setClickListener(IClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface IClickListener{
        void doClick(int pos,BeanIntput beanIntput);
    }

}
