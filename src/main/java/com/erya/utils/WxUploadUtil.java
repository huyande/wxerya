package com.erya.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 上传图片到微信公众号
 *
 */
public class WxUploadUtil {



    private static Logger log = LoggerFactory.getLogger(WxUploadUtil.class);


    /**
     * 	根据不同的链接 将素材上传到微信服务器上
     * @param url 上传地址
     * @param file 待上传文件
     * @return 返回一个map 集合  存放着微信服务器返回的数据
     * 
     */
    public static Map<String,String> wxUploadFile(String url, InputStream file,String fileName) throws IOException{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(url);

            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000).build();
            httppost.setConfig(requestConfig);

            /**
		       * 这一步最关键：
		       *调用addBinaryBody("media",input2byte(file),ContentType.DEFAULT_BINARY, fileName),
             * fileName可以为任意值，但不能为null，如果为null则上传失败。
             * input2byte(file)：把inputstream转为byte[]，
		         * 如果直接调用addPart用FileBody做参数，则MultifilePart不好转换；
		          * 如果直接调用addPart用InpustreamBody做参数，则因为没有fileName会造成上传失败
             */
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addBinaryBody("media", input2byte(file), ContentType.DEFAULT_BINARY, 
                                    fileName).build();

            httppost.setEntity(reqEntity);
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                //System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String responseEntityStr = EntityUtils.toString(response.getEntity());
                    //log.info("responseEntityStr=[{}]",responseEntityStr);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String,String> map = objectMapper.readValue(responseEntityStr, Map.class);
                    return  map;
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
    private static final byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }


    public static void main(String[] args) throws IOException {
    	String filename="F:\\wxImage\\FsBcSWQEuC6j_QmadAzoXSZzx6zH.jpg";
    	File file = new File(filename);
    	InputStream in = new FileInputStream(file);
    	// 上传微信永久素材的url
        String UPLOAD_FOREVER_MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";

        //上传图文消息中的图片
        String UPLOAD_ARTICLE_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN";
        
        String replacedUrl = UPLOAD_FOREVER_MEDIA_URL
                    .replace("ACCESS_TOKEN", "24_J5E0lAjUXVRWbJNQyxnlbGWCknhIyT1FByzKATlk-i2-LcGb5XZczV3LV1TNkVgevv7siZLqMWNcJgM0H4P5_x7kEy4ZyZ96PrtyFgCFy_l-Ra0ImHpQPXkFrnK_qELvXC2VkdEK62Vz4cWLIGZjACARPI")
                    .replace("TYPE", "image");


        String replaceArt_url =UPLOAD_FOREVER_MEDIA_URL.replace("ACCESS_TOKEN", "24_QbU6EKvmRMt5ujiLYlgE7yxAtNAt5b2FMF5TZ4Cs8kz5K_aNLvCRYgCNWhnRk9Qs_zix4mYY52kdwfspDsSMzDJEVneDzOW-b9YYhVI8MZoCw9Z54FDxgmxR2DXBD6YZrgAEmA5dJ6CG_PorKXYcAGAFST");


        Map<String,String> mapParam = WxUploadUtil.wxUploadFile(replacedUrl, in,filename);
		System.out.println(mapParam.get("media_id"));
		System.out.println(mapParam);
        //log.info("微信素材上传结果：[{}]", jsonObject.toString());
	}

}
