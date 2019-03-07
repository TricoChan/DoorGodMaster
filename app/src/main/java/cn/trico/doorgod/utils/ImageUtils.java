package cn.trico.doorgod.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * 图片缓存
 *
 * @author Trico
 * @since 2018/3/26
 */

public class ImageUtils {

    private static ImageView mView;
    private static String mImgUrl;
    private static int mPosition;

    private ImageUtils() {//不允许被外部调用构造函数
    }

    /**
     * 加载图片工具类
     * <p>
     * 内存中存在则直接拿图片，不存在则从网上加载
     *
     * @param view    要加载到的view上
     * @param imgUrl  要加载的imgUrl
     * @param context 上下文
     */
    public static void loadImage(ImageView view, String imgUrl, Context context, int position) {
        mView = view;
        mImgUrl = imgUrl;
        mPosition = position;
        new ImageCacheAsyncTask(context).execute(imgUrl);
    }

    static class ImageCacheAsyncTask extends AsyncTask<String, Void, File> {

        private final Context context;

        public ImageCacheAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected File doInBackground(String... params) {
            String imgUrl = params[0];
            try {
                return Glide.with(context)
                        .load(imgUrl)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } catch (ExecutionException e) {

                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(File file) {
            if (file == null) {//本地无缓存从网络加载
                // if ((int) mView.getTag() == mPosition) {
                Glide.with(context)
                        .load(mImgUrl)
                        .into(mView);
                return;
                //  }
            }
            if ((int) mView.getTag() == mPosition) {
                Bitmap bmp = BitmapFactory.decodeFile(file.getPath());
                mView.setImageBitmap(bmp);
            }
        }
    }
}