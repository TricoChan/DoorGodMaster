package cn.trico.doorgod.entity;

import java.io.Serializable;

public class DeviceInfoItem implements Serializable {
    private String head;
    private String body;

    public DeviceInfoItem(String head, String body){
        this.head = head;
        this.body = body;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
