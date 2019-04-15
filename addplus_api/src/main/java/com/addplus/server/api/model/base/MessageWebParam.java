package com.addplus.server.api.model.base;


import java.io.Serializable;
import java.util.Map;

/**
 * 类名: MessageWebParam
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2017/12/21 下午4:41
 * @description 类描述: Websocket入参封装类
 */
public class MessageWebParam implements Serializable {
    private static final long serialVersionUID = -919703297761657457L;

    /**
     * 用户主键id
     */
    private Integer userId;


    /**
     * 请求类型,对象类型 post, 其他的get。按回http请求类型
     */
    private String path;

    /**
     * 请求入参
     */
    private Map params;

    /**
     * 返回订阅路径
     */
    private String subscribe;

    /**
     * 请求时间戳
     */
    private Long timestamp;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }

    public String getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(String subscribe) {
        this.subscribe = subscribe;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
