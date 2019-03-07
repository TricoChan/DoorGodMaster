package cn.trico.doorgod.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import cn.trico.doorgod.R;
import cn.trico.doorgod.fragment.BaseFragment;
import cn.trico.doorgod.fragment.ControlFragment;
import cn.trico.doorgod.fragment.InfoFragment;
import cn.trico.doorgod.fragment.UserFragment;
import cn.trico.doorgod.utils.TabItem;
import cn.trico.doorgod.utils.TabLayout;

public class MainActivity extends BaseActivity implements TabLayout.OnTabClickListener {

    //private ActionBar actionBar;
    BaseFragment fragment;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<TabItem> tabs;

    @Override
    public void setTranslucentStatusToLollipop() {
        isTranslucent(true);
        super.setTranslucentStatusToLollipop();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_main_layout;
    }

    @Override
    protected void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        /*
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }
        */
    }

    @Override
    protected void initData() {
        tabs = new ArrayList<>();
        tabs.add(new TabItem(R.drawable.selector_tab_info, R.string.title_tab_info, InfoFragment.class));
        tabs.add(new TabItem(R.drawable.selector_tab_set, R.string.title_tab_set, ControlFragment.class));
        tabs.add(new TabItem(R.drawable.selector_tab_administrator, R.string.title_tab_administrator, UserFragment.class));
        mTabLayout.initData(tabs, this);
        mTabLayout.setCurrentTab(0);
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(1);//预先加载左右各1个界面
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
                //actionBar.setTitle(tabs.get(position).lableResId);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onTabClick(TabItem tabItem) {
        //actionBar.setTitle(tabItem.lableResId);
        mViewPager.setCurrentItem(tabs.indexOf(tabItem));

    }


    public class FragAdapter extends FragmentPagerAdapter {


        public FragAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            try {
                return tabs.get(arg0).tagFragmentClz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return tabs.size();
        }
    }
}