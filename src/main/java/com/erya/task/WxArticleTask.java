package com.erya.task;

import com.erya.bean.bo.WxArticles;
import com.erya.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WxArticleTask {
	//上传图文消息中的图片 微信返回 图片url
	private String  UPLOAD_ARTICLE_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN";

	//上传永久素材 返回mediaId
	String UPLOAD_FOREVER_MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";

	@Value("${wx.saveToken}")
	private String tokenFilePath;

	@Value("${wx.appid}")
	private String appid;

	@Value("${wx.secret}")
	private String secret;

	@Value("${wx.saveIntnetImagePath}")
	private String imagePath;


	@Scheduled(cron = "0 0 7 * * ? ")
	public void articleCreateTask(){
		String token = WxAccessTokenUtil.getAccessToken(tokenFilePath,appid,secret);
		System.out.println(token);
		log.info("开始执行创建素材的定时任务");
		try {
			//爬取one 网站相关信息 获取信息
			String html = HttpClientUtil.getHtml("http://wufazhuce.com");
			//获取到图片地址和内容的map 对象
			Map<String, String> one_map = JsoupUtils.parserHtml(html);
			//获取图片地址 将图片保存到本地
			String imgUrl = "";
			String text = "";
			String img_name=""; //图片的名称
			if(one_map!=null){
				for(String key : one_map.keySet()){
					imgUrl = key;
					text = one_map.get(key);
				}
				//更具url 下载图片到本地
				//截取字符串
				img_name = imgUrl.substring(imgUrl.lastIndexOf("/") + 1, imgUrl.length());
				DownloadPicFromURLUtil.downloadFromUrl(imgUrl,imagePath+img_name+".jpg");
			}

			//上传照片 获取图片的url
			String filename = imagePath+img_name+".jpg";
			File file = new File(filename);
			InputStream in = new FileInputStream(file);
			String replaceArt_url =UPLOAD_ARTICLE_URL.replace("ACCESS_TOKEN",token);
			Map<String,String> mapParam = WxUploadUtil.wxUploadFile(replaceArt_url, in,filename);

			//获取到上传到微信服务器 返回过来的url
			String wx_image_url = mapParam.get("url");

			//生成html 字符串
			Map<String,String> wxData =  new HashMap<>();
			wxData.put("one_text",text);
			wxData.put("wx_image_url",wx_image_url);
			wxData.put("from","校园表白鹿");
			String oneHtml = CreateHtmlUtils.creteOneHtml("tempalte.ftl", wxData);

			//上传素材到 微信公众号
			WxArticles wxArticles = new WxArticles();
			wxArticles.setTitle("Vol.1");
			wxArticles.setContent(oneHtml);
			wxArticles.setAuthor("校园服务");
			wxArticles.setDigest(text.substring(0,text.indexOf("。"))+"...");
			wxArticles.setNeed_open_comment(1);
			wxArticles.setOnly_fans_can_comment(0);
			wxArticles.setShow_cover_pic(0);

			//设置素材id
			String forever_url = UPLOAD_FOREVER_MEDIA_URL.replace("ACCESS_TOKEN", token).replace("TYPE", "image");
			File file_f = new File(filename);
			InputStream in_f = new FileInputStream(file_f);
			Map<String,String> forever_map = WxUploadUtil.wxUploadFile(forever_url, in_f,filename);

			wxArticles.setThumb_media_id(forever_map.get("media_id"));
			CreateArticlesUtils.createArticles(wxArticles,token);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		WxArticleTask wxArticleTask = new WxArticleTask();
		wxArticleTask.articleCreateTask();
		/*try {
			//爬取one 网站相关信息 获取信息
			String html = HttpClientUtil.getHtml("http://wufazhuce.com");
			//获取到图片地址和内容的map 对象
			Map<String, String> one_map = JsoupUtils.parserHtml(html);
			//获取图片地址 将图片保存到本地
			String imgUrl = "";
			String text = "";
			String img_name=""; //图片的名称
			if(one_map!=null){
				for(String key : one_map.keySet()){
					imgUrl = key;
					text = one_map.get(key);
				}
				//更具url 下载图片到本地
				//截取字符串
				img_name = imgUrl.substring(imgUrl.lastIndexOf("/") + 1, imgUrl.length());
				DownloadPicFromURLUtil.downloadPicture(imgUrl,"F:\\wxImage\\"+img_name+".jpg");
			}

			//上传照片 获取图片的url
			String filename = "F:\\wxImage\\"+img_name+".jpg";
			File file = new File(filename);
			InputStream in = new FileInputStream(file);
			String replaceArt_url =UPLOAD_ARTICLE_URL.replace("ACCESS_TOKEN",token);
			Map<String,String> mapParam = WxUploadUtil.wxUploadFile(replaceArt_url, in,filename);

			//获取到上传到微信服务器 返回过来的url
			String wx_image_url = mapParam.get("url");

			//生成html 字符串
			Map<String,String> wxData =  new HashMap<>();
			wxData.put("one_text",text);
			wxData.put("wx_image_url",wx_image_url);
			String oneHtml = CreateHtmlUtils.creteOneHtml("F:/wxImage/tempalte.ftl", wxData);

			//上传素材到 微信公众号
			WxArticles wxArticles = new WxArticles();
			wxArticles.setTitle("Vol-1");
			wxArticles.setContent(oneHtml);
			wxArticles.setAuthor("校园服务");
			wxArticles.setDigest("简介");
			wxArticles.setNeed_open_comment(1);
			wxArticles.setOnly_fans_can_comment(0);
			wxArticles.setShow_cover_pic(0);
			//设置素材id
			wxArticles.setThumb_media_id("HOLogY_s_mnxmPpgIiN43cyggBGmE9NuYUbs382mmC0");
			CreateArticlesUtils.createArticles(wxArticles,token);

		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

}
