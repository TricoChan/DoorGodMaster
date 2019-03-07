package cn.trico.doorgod.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 首页list对应json实体类
 */

public class ImageInfo implements Serializable{
    private String err_code;
    private String msg;
    private List<ImageBean> res;

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ImageBean> getRes() {
        return res;
    }

    public void setRes(List<ImageBean> res) {
        this.res = res;
    }

    public class ImageBean implements Serializable {
        private String image_url;
        private String create_time;

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
    }
}
