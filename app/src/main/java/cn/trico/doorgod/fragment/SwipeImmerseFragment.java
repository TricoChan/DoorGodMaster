package cn.trico.doorgod.fragment;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.util.DensityUtil;

import cn.trico.doorgod.R;
import cn.trico.doorgod.application.App;
import cn.trico.doorgod.utils.StatusBarUtils;

public abstract class SwipeImmerseFragment extends BaseFragment {

    public RefreshLayout refreshLayout;
    public Toolbar toolbar;
    public NestedScrollView scrollView;
    public ButtonBarLayout buttonBar;
    public ImageView parallax;
    public int mOffset = 0;
    public int mScrollY = 0;

    @Override
    protected void initView() {
        parallax = mContentView.findViewById(R.id.parallax);
        buttonBar = mContentView.findViewById(R.id.buttonBarLayout);
        scrollView = (NestedScrollView) mContentView.findViewById(R.id.scrollView);
        refreshLayout = (RefreshLayout) mContentView.findViewById(R.id.info_refresh_layout);
        toolbar = (Toolbar) mContentView.findViewById(R.id.toolbar);
        //状态栏透明和间距处理
        StatusBarUtils.immersive(getActivity());
        StatusBarUtils.setPaddingSmart(getContext(), toolbar);
        refreshLayout.setEnableLoadMore(false);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            private int lastScrollY = 0;
            private int h = DensityUtil.dp2px(170);
            private int color = ContextCompat.getColor(App.getInstance(), R.color.colorPrimary) & 0x00ffffff;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    buttonBar.setAlpha(1f * mScrollY / h);
                    toolbar.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                    parallax.setTranslationY(mOffset - mScrollY);
                }
                lastScrollY = scrollY;
            }
        });
        buttonBar.setAlpha(0);
        toolbar.setBackgroundColor(0);
    }

    @Override
    public void fetchData() {
    }
}
