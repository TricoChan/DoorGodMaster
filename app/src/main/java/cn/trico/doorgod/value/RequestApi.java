package cn.trico.doorgod.value;

public class RequestApi {
    /**
     * 发送指令接口
     */
    public static final String SEND_CONTROL_COMMAND = "http://47.101.202.111:3001/user/postCommand";
    /**
     * 更新用户信息接口
     */
    public static final String UPDATE_USER_INFO = "http://47.101.202.111:3001/user/updateUserInfo";
    /**
     * 激活/注册设备接口
     */
    public static final String DEVICE_ACTIVATION = "http://47.101.202.111:3001/user/registerDevice";
    /**
     * 用户登录接口
     */
    public static final String USER_LOGIN = "http://47.101.202.111:3001/user/login";
    /**
     * 获取用户信息接口
     */
    public static final String GET_USER_INFO = "http://47.101.202.111:3001/user/getUserInfo";
    /**
     * 获取设备信息接口
     */
    public static final String GET_DEVICE_INFO = "http://47.101.202.111:3001/user/getDeviceInfo";
    /**
     * 获取设备图像接口
     */
    public static final String GET_IMAGE = "http://47.101.202.111:3001/user/getDeviceImage";
    /**
     * 用户注册接口
     */
    public static final String USER_REGISTER = "http://47.101.202.111:3001/user/register";
}
