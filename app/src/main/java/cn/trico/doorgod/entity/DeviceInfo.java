package cn.trico.doorgod.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by Trico on 2018/3/7.
 */

public class DeviceInfo {
    private String err_code;
    private String msg;
    private InfoDetails res;

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

    public InfoDetails getRes() {
        return res;
    }

    public void setRes(InfoDetails res) {
        this.res = res;
    }

    public class InfoDetails {
        private String electricity;
        private String lock_state;
        private String knock_state;
        private String poke_state;
        private String create_time;
        @SerializedName("31")
        private String extend_module1;
        @SerializedName("32")
        private String extend_module2;
        @SerializedName("33")
        private String extend_module3;
        @SerializedName("34")
        private String extend_module4;
        @SerializedName("35")
        private String extend_module5;

        public String getExtend_module1() {
            return extend_module1;
        }

        public void setExtend_module1(String extend_module1) {
            this.extend_module1 = extend_module1;
        }

        public String getExtend_module2() {
            return extend_module2;
        }

        public void setExtend_module2(String extend_module2) {
            this.extend_module2 = extend_module2;
        }

        public String getExtend_module3() {
            return extend_module3;
        }

        public void setExtend_module3(String extend_module3) {
            this.extend_module3 = extend_module3;
        }

        public String getExtend_module4() {
            return extend_module4;
        }

        public void setExtend_module4(String extend_module4) {
            this.extend_module4 = extend_module4;
        }

        public String getExtend_module5() {
            return extend_module5;
        }

        public void setExtend_module5(String extend_module5) {
            this.extend_module5 = extend_module5;
        }

        public String getElectricity() {
            return electricity;
        }

        public void setElectricity(String electricity) {
            this.electricity = electricity;
        }

        public String getLock_state() {
            return lock_state;
        }

        public void setLock_state(String lock_state) {
            this.lock_state = lock_state;
        }

        public String getKnock_state() {
            return knock_state;
        }

        public void setKnock_state(String knock_state) {
            this.knock_state = knock_state;
        }

        public String getPoke_state() {
            return poke_state;
        }

        public void setPoke_state(String poke_state) {
            this.poke_state = poke_state;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}