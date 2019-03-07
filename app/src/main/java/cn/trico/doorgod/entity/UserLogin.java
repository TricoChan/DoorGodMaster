package cn.trico.doorgod.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 用户登录返回JSON实体类
 */
public class UserLogin {
    private String err_code;
    private String msg;
    private ResGetToken res;

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

    public ResGetToken getRes() {
        return res;
    }

    public void setRes(ResGetToken res) {
        this.res = res;
    }

    public class DeviceId {
        private String device_id;

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }
    }

    public class ResGetToken {
        private String token;
        private List<DeviceId> device;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public List<DeviceId> getDevice() {
            return device;
        }

        public void setDevice(List<DeviceId> device) {
            this.device = device;
        }
    }
}
