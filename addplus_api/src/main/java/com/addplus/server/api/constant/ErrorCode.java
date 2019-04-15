package com.addplus.server.api.constant;

/**
 * 类名: ErrorCode
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/8/29 下午5:29
 * @description 类描述: 异常码枚举类
 */
public enum ErrorCode {
    //系统-公共基础类
    SYS_SUCCESS("000000", "success"),
    SYS_ERROR_SERVICE("000001", "服务器内部错误"),
    SYS_ERROR_TYPE_LENGTH("000002", "入参参数不一致"),
    SYS_ERROR_NOT_TOKEN("000003", "需要token鉴权"),
    SYS_ERROR_TOKEN_EXPIRE("000004", "token过期"),
    SYS_ERROR_TOKEN_ERROR("000005", "非法token"),
    SYS_ERROR_NULLDATE("000006", "无数据"),
    SYS_ERROR_DATABASEFAIL("000007", "数据库操作失败"),
    SYS_ERROR_PARAM("000008", "入参参数缺失"),
    SYS_ERROR_NOT_AVAILABLE("000009", "无可更新的内容"),
    SYS_ERROR_PATH("000010", "接口路径不存在"),
    SYS_ERROR_NO_MODIFI_ALLOW("000011", "当前内容不允许修改"),
    SYS_ERROR_DATA_EXIT("000012", "数据已存在"),
    SYS_ERROR_REQUEST_TIMEOUT("000013", "网络超时,请重试!"),
    //登录类型错误码
    SYS_LOGIN_LOGGED_IN("010000", "已登录"),
    SYS_LOGIN_UNLOGIN("010001", "未登录"),
    SYS_LOGIN_UNAUTHORITY("010002", "未授权"),
    SYS_LOGIN_CREDENTIAL_ERROE("010003", "密码错误"),
    SYS_LOGIN_ACCOUNT_LOCK("010004", "账号已锁定"),
    SYS_LOGIN_VERIFY_ERROR("010005", "验证码错误或失效"),
    SYS_LOGIN_MEMBER_DISABLE("010006", "账号已冻结"),
    SYS_LOGIN_MEMBER_DELETE("010007", "账号已被删除");

    private String code;

    private String error;

    ErrorCode(String code, String error) {
        this.code = code;
        this.error = error;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
