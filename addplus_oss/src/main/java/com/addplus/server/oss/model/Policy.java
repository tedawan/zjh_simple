package com.addplus.server.oss.model;

/**
 * @author fuyq
 * @date 2018/8/30
 */
public class Policy {

    private String OSSAccessKeyId;

    private String policy;

    private String key;

    private String dir;

    private String signature;

    private String host;

    private String expire;

    public String getOSSAccessKeyId() {
        return OSSAccessKeyId;
    }

    public void setOSSAccessKeyId(String OSSAccessKeyId) {
        this.OSSAccessKeyId = OSSAccessKeyId;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }
}
