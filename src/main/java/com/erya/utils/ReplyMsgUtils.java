package com.erya.utils;

import com.erya.bean.bo.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * 消息回复 工具方法 
 * @author Administrator
 *
 */
public class ReplyMsgUtils {

	/**
	 * 发送图片的接口 
	 * @param msgXmlMap
	 * @param accessMap
	 * @param WxUser
	 * @param appid
	 * @param secret
	 * @param wXfileImagePath
	 * @return
	 */
	public  static String replyImageMsg(Map<String, String> msgXmlMap,Map<String,Date> accessMap,String WxUser,String appid,String secret,String wXfileImagePath) {
	    //素材id
	    String media_id = "";
		//获取图片的地址 上传图片
		String PicUrl = msgXmlMap.get("PicUrl");
		//上传图片到素材库中 
		media_id = uploadImage(PicUrl, wXfileImagePath, accessMap, appid, secret);
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setToUserName(msgXmlMap.get("FromUserName"));
        imageMessage.setFromUserName(WxUser);
        imageMessage.setCreateTime(new Date().toString());
        Image image = new Image();
        image.setMediaId(media_id);
		imageMessage.setImage(image);
		imageMessage.setMsgType("image");
		String sendImageMsg = sendImageMsg(imageMessage);
		return sendImageMsg;
	}
	
	/**
	 * 回复文本消息
	 * @param msgXmlMap
	 * @param WxUser
	 * @return
	 */
	public static String replyTextMsg(Map<String, String> msgXmlMap,String WxUser,String replyStr) {
		TextMessage textMessage = new TextMessage();
		textMessage.setToUserName(msgXmlMap.get("FromUserName"));
		textMessage.setFromUserName(WxUser);
		textMessage.setCreateTime(new Date().toString());
		textMessage.setMsgType(WXmlUtils.MESSAGE_TEXT);
		textMessage.setContent(replyStr);
		String sendTextMsg = sendTextMsg(textMessage);
		return sendTextMsg;
	}

	/**
	 * 回复图文信息
	 * @param msgXmlMap
	 * @param WxUser
	 * @return
	 */
	public static String replyImageTextMsg(Map<String, String> msgXmlMap,String WxUser,String url) {
		ImageTextMessage imageTextMessage = new ImageTextMessage();
		imageTextMessage.setFromUserName(WxUser);
		imageTextMessage.setToUserName(msgXmlMap.get("FromUserName"));
		imageTextMessage.setCreateTime(new Date().getTime());
		imageTextMessage.setMsgType("news");
		imageTextMessage.setArticleCount(1);
		List<Article> articles = new ArrayList<>();
		Article article = new Article();
		//article.setDescription("想做一些更好的功能，所以这篇文章需要一些点击量，这会使我获得一些报酬，来购买服务器。");
		//article.setTitle("帮忙点下此文章，我想换个牛X的服务器。");
		article.setDescription("今日公众号查询题目次数已用完，点击我使用微信小程序查询");
		article.setTitle("今日查询次数已用完，移步微信小程序不限查询");
		article.setPicUrl("https://cn.bing.com/th?id=OIP.VuJu-rFWose_hhkroSHrsQHaFj&pid=Api&rs=1");
		article.setUrl(url);
		articles.add(article);
		imageTextMessage.setArticles(articles);
		return sendImageTextMsg(imageTextMessage);
	}

	
	/**
	 *  生成文本消息
	 * @param textMessage
	 * @return
	 */
	public static String sendTextMsg(TextMessage textMessage) {
		return WXmlUtils.textMessageToXml(textMessage);
	}
	/**
	 * 生成图片消息
	 * @param imageMessage
	 * @return
	 */
	public static String sendImageMsg(ImageMessage imageMessage) {
		return WXmlUtils.imageMessageToXml(imageMessage);
	}

	/**
	 * 生成图文消息
	 * @param imageMessage
	 * @return
	 */
	public static String sendImageTextMsg(ImageTextMessage imageTextMessage) {
		return WXmlUtils.imageTextMessageToXml(imageTextMessage);
	}




	/**
	 * 获取到消息中的图片地址 将其上传到素材中
	 * 
	 * @return 返回 素材id
	 */
	public static String uploadImage(String PicUrl,String wXfileImagePath,Map<String,Date> accessMap,String appid,String secret) {
		String UPLOAD_FOREVER_MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";
		//素材id
	    String media_id = "";
		//下载图片到本地
		String realpath = DownloadPicFromURLUtil.downloadFromUrl(PicUrl, wXfileImagePath+UUID.randomUUID().toString()+".png");
		
		//调用获取token 
		//String accessToken = WxAccessTokenUtil.getAccessToken(appid, secret);
		//Map<String,Date> accessMap = new HashMap<String, Date>();
		WxAccessTokenUtil.iSaccessTokenTimeOut(accessMap,appid, secret);
		String accessToken ="";
		for(Map.Entry<String, Date> entry: accessMap.entrySet()) {
			accessToken = entry.getKey();
		}
		System.out.println(accessToken);
		//获取到本地的下载的图片 
		String replacedUrl = UPLOAD_FOREVER_MEDIA_URL
                .replace("ACCESS_TOKEN", accessToken)
                .replace("TYPE", "image");
		File file = new File(realpath);
		InputStream in;
		try {
			in = new FileInputStream(file);
			Map<String,String> mapParam = WxUploadUtil.wxUploadFile(replacedUrl, in, realpath);
			media_id = mapParam.get("media_id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return media_id;
	}

}
