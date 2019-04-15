package com.addplus.server.oss;

import com.addplus.server.oss.config.OssConfig;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;

/**
 * 类描述: OssClient工具类
 *
 * @author fyq
 * @version V1.0
 * @date 2017年05月23日 15:02
 */
@Component
public class OssClientUtils {

    @Autowired
    private OssConfig ossCfg;

    private static OssConfig ossConfig;

    private static final String THUMBNAIL = "{0}?x-oss-process=image/resize,m_mfit,h_{1},w_{2}";

    private static final String HTTPS = "https://";

    @PostConstruct
    public void init() {
        ossConfig = ossCfg;
    }

    public static OSS createOssClient(String accessKeyId, String accessKeySecret, String endpoint) {
        ClientBuilderConfiguration configuration = new ClientBuilderConfiguration();
        configuration.setMaxConnections(200);
        configuration.setSocketTimeout(10000);
        configuration.setMaxErrorRetry(5);
        String httpEndpoint = HTTPS.concat(endpoint);
        return new OSSClientBuilder().build(httpEndpoint, accessKeyId, accessKeySecret, configuration);
    }

    public static OSS createOssClient() {
        return createOssClient(ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret(), ossConfig.getEndpoint());
//        return null;
    }

    public static String getServerUrl(String path, String endpoint, String bucket) {
        return HTTPS + bucket + "." + endpoint + "/" + path;
    }

    public static String imageThumbnail(String path, Integer width, Integer height) {
        return MessageFormat.format(THUMBNAIL, path, height, width);
    }

    public static String getAccessKeyId() {
        return ossConfig.getAccessKeyId();
    }

    public static String getAccessKeySecret() {
        return ossConfig.getAccessKeySecret();
    }

    public static String getEndpoint() {
        return ossConfig.getEndpoint();
    }

    public static String getBucket() {
        return ossConfig.getBucket();
    }
}
