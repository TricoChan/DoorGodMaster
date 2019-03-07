package cn.trico.doorgod.adapter;

import android.content.Context;

import java.util.List;

import cn.trico.doorgod.CustomRecyclerView.CommonBaseAdapter;
import cn.trico.doorgod.CustomRecyclerView.ViewHolder;
import cn.trico.doorgod.R;
import cn.trico.doorgod.entity.ImageInfo;
import cn.trico.doorgod.utils.TimeUtils;

public class HomeImageAdapter extends CommonBaseAdapter<ImageInfo.ImageBean> {

    public HomeImageAdapter(Context context, List<ImageInfo.ImageBean> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected void convert(ViewHolder holder, ImageInfo.ImageBean data, int position) {
        String time[] = TimeUtils.utc2Local(data.getCreate_time(), TimeUtils.DIGITAL_TIME).split(" ");
        holder.setText(R.id.tv_create_date, time[0]);
        holder.setText(R.id.tv_create_time, time[1]);
        holder.setImage(R.id.iv_home_list, data.getImage_url());
        //Log.d("FFFFFFFFFFFFFFFFF", "onBindViewHolder: " + position);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.home_list_item;
    }
}

