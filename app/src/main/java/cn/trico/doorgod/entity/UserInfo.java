package cn.trico.doorgod.entity;

public class UserInfo {
    private String err_code;
    private String msg;
    private DetailInfo res;

    public class DetailInfo {
        private String id;
        private String phone;
        private String name;
        private String avatar_url;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }
    }

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

    public DetailInfo getRes() {
        return res;
    }

    public void setRes(DetailInfo res) {
        this.res = res;
    }
}
