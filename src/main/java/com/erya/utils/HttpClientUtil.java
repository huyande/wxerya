package com.erya.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
public class HttpClientUtil {
	
	 // 连接主机超时（30s）
    public static final int HTTP_CONNECT_TIMEOUT_30S = 30 * 1000;

    // 从主机读取数据超时（3min）
    public static final int HTTP_READ_TIMEOUT_3MIN = 180 * 1000;
	
	/**
	 * get 方法
	 * @param Url
	 * @return
	 * @throws IOException
	 */
	public static String get(String Url) throws IOException {
        CloseableHttpClient httpClient=HttpClients.createDefault();
        //创建httpGet
        HttpGet httpGet=new HttpGet(Url);
        //System.out.println("URL is "+httpGet.getURI());
        CloseableHttpResponse response = null;
        try {
            //执行http请求
            response=httpClient.execute(httpGet);
            //获取http响应体
            HttpEntity entity=response.getEntity();
//            System.out.println("-----------------");
            //打印响应状态
//            System.out.println(response.getStatusLine());
            if(entity!=null) {
//                System.out.println("Response Content Length:"+entity.getContentLength());
                String content=EntityUtils.toString(entity);
//                System.out.println("Response Content:"+content);
                return content;
            }
            System.out.println("------------------");

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            response.close();
            httpClient.close();
        }
        return null;
    }
	
	/**
     * httpPost
     */
    public static String httpPost(String url, String jsonParam) throws ClientProtocolException, IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        // 设置请求头和请求参数
        if (null != jsonParam && !jsonParam.isEmpty()) {
            StringEntity entity = new StringEntity(jsonParam, "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
        }

        // 超时时间设置
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(HTTP_READ_TIMEOUT_3MIN)
                .setConnectTimeout(HTTP_CONNECT_TIMEOUT_30S).build();
        httpPost.setConfig(requestConfig);

        // 发送请求
        CloseableHttpResponse response = httpclient.execute(httpPost);

        // 获取返回内容
        try {
            HttpEntity entity = response.getEntity();
            String str = EntityUtils.toString(entity);
            EntityUtils.consume(entity); // 此句关闭了流
            return str;
        } finally {
            response.close();
        }
    }
}
