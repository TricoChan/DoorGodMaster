package cn.trico.doorgod.CustomRecyclerView;

public interface OnSwipeMenuClickListener<T> {
    void onSwipMenuClick(ViewHolder viewHolder, T data, int position);
}