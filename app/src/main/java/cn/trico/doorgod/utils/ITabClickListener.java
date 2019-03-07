package cn.trico.doorgod.utils;

import cn.trico.doorgod.fragment.BaseFragment;
/**
 * Created by yx on 16/4/6.
 */
public interface ITabClickListener {


    void onMenuItemClick();

    BaseFragment getFragment();
}
