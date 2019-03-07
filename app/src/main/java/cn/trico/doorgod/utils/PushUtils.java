package cn.trico.doorgod.utils;

import android.content.Context;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;

public class PushUtils {

    public static final int ACTION_SET = 2;
    public static int sequence = 1;
//    private String alias;
////    private boolean isAliasAction;
////    private int action;

    public static void setAlias(Context context, String alias, boolean isAliasAction, int action) {
        TagAliasBean tagAliasBean = new TagAliasBean();
        tagAliasBean.action = action;
        sequence++;
        tagAliasBean.alias = alias;
        tagAliasBean.isAliasAction = isAliasAction;
        JPushInterface.setAlias(context, sequence, tagAliasBean.alias);
    }

    public static class TagAliasBean {
        int action;
        Set<String> tags;
        String alias;
        boolean isAliasAction;

        @Override
        public String toString() {
            return "TagAliasBean{" +
                    "action=" + action +
                    ", tags=" + tags +
                    ", alias='" + alias + '\'' +
                    ", isAliasAction=" + isAliasAction +
                    '}';
        }
    }
}
