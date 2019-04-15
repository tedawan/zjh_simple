package com.addplus.server.api.constant;

/**
 * 类描述:存放公共的常量值
 *
 * @author ljt
 * @version V1.0
 * @date
 */
public class StringConstant {

    /**
     * http请求方式
     */
    public final static String REQUEST_HTTP = "http";

    /**
     * ws请求方式
     */
    public final static String REQUEST_WEBSOCKET = "ws";

    /**
     * 请求参数token的键
     */
    public final static String REQ_TOKEN_KEY = "token_define";

    /**
     * token的键
     */
    public final static String TOKEN = "token";

    /**
     * 不需要token
     */
    public final static String NOT_TOKEN = "noToken";

    /**
     * 需要token
     */
    public final static String NEED_TOKEN = "needToken";

    /**
     * ip地址
     */
    public final static String IP_ADDRESS = "ipAddress";

    /**
     * 接口类型
     */
    public final static String MODEL = "model";

    /**
     * token存入redis的前缀
     */
    public final static String REDIS_LOG_RRCORD_TPOIC = "log_record";

    /**
     * log日志记录发送者topic
     */
    public final static String TOKEN_REDIS_PREFIX = "token_rest:";

    /**
     * 不需要的字符串
     */
    public final static String NOCONFIRM = "1";

    /**
     * Redis数据字典
     */
    public final static String REDIS_DATA_DICTIONARY = "redis_data_dictionary:";

    /**
     * shrio 请求登录次数
     */
    public final static String REDIS_PREFIX = "shrio_login_count:{0}:{1}";

    /**
     * url动态刷新消息发送者topic
     */
    public final static String REDIS_SHRIO_PERMISSION_TPOIC = "url_filter";

    /**
     * HTTP请求头部鉴权
     */
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

    /**
     * HTTP请求头部域
     */
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    /**
     * 请求头授权验证
     */
    public static final String ACCESS_CONTROL_ALLOW_AUTHORIZATION = "Authorization";

    /**
     * 验证码请求前缀
     */
    public static final String MESSAGE_VERIFICATION_CODE = "verification_Code:{0}:{1}:{2}";

    /**
     * 产品名称:云通信短信API产品
     */
    public static final String ALIYUN_SMS_PRODUCT = "Dysmsapi";

    /**
     * 产品域名
     */
    public static final String ALIYUN_SMS_DOMAIN = "dysmsapi.aliyuncs.com";

    /**
     * 产品区域
     */
    public static final String ALIYUN_SMS_REGIONID = "cn-hangzhou";

    /**
     * 短信签名
     */
    public static final String ALIYUN_SMS_SIGNNAME = "小镇商城";
}
