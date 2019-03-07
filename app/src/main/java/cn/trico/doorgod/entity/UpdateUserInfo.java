package cn.trico.doorgod.entity;

/**
 * 注册/更新用户信息JSON返回实体类
 */
public class UpdateUserInfo {
    private String err_code;
    private String msg;

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
}
