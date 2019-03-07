package cn.trico.doorgod.utils;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.widget.Toast;

public class ToastUtils {
    public static void toastinThread(Context context, @StringRes int info) {
        Looper.prepare();
        Toast.makeText(context, context.getResources().getText(info), Toast.LENGTH_SHORT).show();
        Looper.loop();
    }
}
