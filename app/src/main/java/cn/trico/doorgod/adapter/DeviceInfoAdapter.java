package cn.trico.doorgod.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.trico.doorgod.R;
import cn.trico.doorgod.entity.DeviceInfoItem;

public class DeviceInfoAdapter extends RecyclerView.Adapter<DeviceInfoAdapter.ViewHolder>{

    private Context context;
    private List<DeviceInfoItem> datas;
    private onItemClickListener onItemClickListener;
    public static final int ONE_ITEM = 1;
    public static final int TWO_ITEM = 2;

    public DeviceInfoAdapter(Context context, List<DeviceInfoItem> datas) {
        this.context = context;
        this.datas = datas;
    }

    public interface onItemClickListener{
        void onItemClick(int position, View view);
    }

    public void setItemClickListener(onItemClickListener itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        DeviceInfoItem deviceInfo = datas.get(position);
        holder.tv_info_head.setText(deviceInfo.getHead());
        holder.tv_info_body.setText(deviceInfo.getBody());
        holder.rl_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(position , v);
               }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if(datas == null){
            return 0;
        }
        return datas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_info_head;
        public TextView tv_info_body;
        public RelativeLayout rl_info;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_info_body = itemView.findViewById(R.id.tv_info_body);
            tv_info_head = itemView.findViewById(R.id.tv_info_head);
            rl_info = itemView.findViewById(R.id.rl_info);
        }
    }
}
