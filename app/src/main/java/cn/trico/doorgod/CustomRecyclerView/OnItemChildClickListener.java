package cn.trico.doorgod.CustomRecyclerView;

public interface OnItemChildClickListener<T> {
    void onItemChildClick(ViewHolder viewHolder, T data, int position);
}
