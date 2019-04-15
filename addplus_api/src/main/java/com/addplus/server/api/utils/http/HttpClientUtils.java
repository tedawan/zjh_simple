package com.addplus.server.api.utils.http;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**类描述:HttpClient连接工具类，当前适用版本是4.5.2
 *
 * @author zhangjiehang
 * @date  2017/5/12 11:28
 * @version V1.0
 */
public class HttpClientUtils {

    private static final String APPLICATION_JSON = "application/json";
    private static final String UTF_8 = "UTF-8";
    private static PoolingHttpClientConnectionManager manager = null;
    private static CloseableHttpClient httpClient = null;


    /**方法描述:获取httpClient
     *
     * @author zhangjiehang
     * @date  2017/5/11 17:41
     * @version V1.0
     */
    public static synchronized CloseableHttpClient getHttpClient() {
        if (httpClient == null) {
            //注册访问协议相关的Socket工厂
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                    .<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", SSLConnectionSocketFactory.getSystemSocketFactory())
                    .build();
            //HttpConnection工厂：配置写请求/解析响应处理器
            HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connectionFactory = new ManagedHttpClientConnectionFactory(DefaultHttpRequestWriterFactory.INSTANCE, DefaultHttpResponseParserFactory.INSTANCE);
            //DNS解析器
            DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;
            //创建池连接管理器
            manager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connectionFactory, dnsResolver);
            //默认为Socket配置
            SocketConfig defaultSocketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
            manager.setDefaultSocketConfig(defaultSocketConfig);
            //设置整个连接池最大连接数
            manager.setMaxTotal(300);
            //每个路由最大链接数
            manager.setDefaultMaxPerRoute(200);
            manager.setValidateAfterInactivity(5 * 1000);

            //默认请求配置
            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setConnectTimeout(2 * 1000) //设置连接超时时间，2s
                    .setSocketTimeout(5 * 1000)//设置等待数据超时时间，5s
                    .setConnectionRequestTimeout(2000).build();

            //创建HttpClient
            httpClient = HttpClients.custom()
                    .setConnectionManager(manager)
                    .setConnectionManagerShared(false) //连接池设置不共享
                    .evictIdleConnections(60, TimeUnit.SECONDS)
                    .evictExpiredConnections() //定期回收空闲连接
                    .setConnectionTimeToLive(60, TimeUnit.SECONDS)
                    .setDefaultRequestConfig(defaultRequestConfig)
                    .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE) //连接重用策略，既是否能keepAlive
                    .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)  //长连接配置，即获取长连接生产多长时间
                    .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).build();

            //JVM停止或重启时，关闭连接池释放掉连接（跟数据库连接类似）
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        httpClient.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return httpClient;
    }


    /**方法描述:http get请求不带参数
     *
     * @param  url 请求地址
     * @return  String
     * @throws   IOException
     * @author zhangjiehang
     * @date  2017/5/11 17:48
     * @version V1.0
     */
    public static String httpGetRequest(String url) throws Exception {
        HttpGet httpGet =new HttpGet(url);
        return getResult(httpGet);
    }


    /**方法描述:实现带参的Http get请求
     *
     * @param  url  请求地址
     * @param  params 参数集合
     * @return  String
     * @throws
     * @author zhangjiehang
     * @date  2017/5/11 17:42
     * @version V1.0
     */
    public static String httpGetWithParams(String url,Map<String, Object> params) throws Exception {
        URIBuilder ub = new URIBuilder();
        ub.setPath(url);
        if(params!=null&&!params.isEmpty()){
            ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
            ub.setParameters(pairs);
        }
        //       ub.setCharset(Charset.forName(UTF_8));
        HttpGet httpGet =new HttpGet(ub.build());
        return getResult(httpGet);

    }

    /**方法描述:实现不带参的Http post请求
     *
     * @param  url  请求地址
     * @return  String
     * @throws
     * @author zhangjiehang
     * @date  2017/5/11 17:42
     * @version V1.0
     */
    public static  String httpPostRequest(String url) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        return getResult(httpPost);
    }


    /**方法描述:实现带参的Http post请求，适用正常的表单提交
     *
     * @param  url  请求地址
     * @param  params  参数集合
     * @return  String
     * @throws
     * @author zhangjiehang
     * @date  2017/5/11 17:42
     * @version V1.0
     */
    public static String httpPostWithRequest(String url,Map<String,Object> params) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        if(params!=null&&!params.isEmpty()){
            ArrayList<NameValuePair> pairs = covertParams2NVPS(params);
            StringEntity se = new UrlEncodedFormEntity(pairs,UTF_8);
            httpPost.setEntity(se);
        }
        return getResult(httpPost);
    }


    /**方法描述:实现带参的Http post请求,适用参数为JSON格式
     *
     * @param  url  请求地址
     * @param  json 参数实体类
     * @return  String
     * @throws
     * @author zhangjiehang
     * @date  2017/5/11 17:42
     * @version V1.0
     */
    public static String httpPostWithRequestJSON(String url,String json) throws Exception {
        HttpPost httpPost = new HttpPost(url);
        StringEntity se = new StringEntity(json,UTF_8);
        se.setContentType(APPLICATION_JSON);
        se.setContentEncoding(UTF_8);
        httpPost.setEntity(se);
        return getResult(httpPost);
    }




    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) {
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
        }
        return pairs;
    }

    /**
     * 处理Http请求
     *
     * @param request
     * @return
     */
    private static String getResult(HttpRequestBase request) throws Exception{
        HttpResponse response=null;
        String result =null;
        try{
            response =getHttpClient().execute(request);
            if(response.getStatusLine().getStatusCode()!= HttpStatus.SC_OK){
                EntityUtils.consume(response.getEntity());
            }else{
                result =EntityUtils.toString(response.getEntity(),UTF_8);
            }
        }catch (Exception e){
            if(response!=null){
                EntityUtils.consume(response.getEntity());
            }

        }
        return result;
    }


}
