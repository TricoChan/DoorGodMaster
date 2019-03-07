package cn.trico.doorgod.CustomRecyclerView;

public interface OnItemClickListener<T> {
    void onItemClick(ViewHolder viewHolder, T data, int position);
}
