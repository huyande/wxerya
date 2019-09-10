package com.erya.service.impl;

import com.erya.bean.bo.WxArticles;
import com.erya.service.WxArticleService;
import com.erya.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class WxArticleServiceImpl implements WxArticleService{

	//上传图文消息中的图片 微信返回 图片url
	private String  UPLOAD_ARTICLE_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN";

	//上传永久素材 返回mediaId
	String UPLOAD_FOREVER_MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";


	@Override
	public String createArt(String imgUrl, String content, String title, String from, String tokenFilePath, String appid, String secret, String imagePath) {
			String token = WxAccessTokenUtil.getAccessToken(tokenFilePath,appid,secret);
			System.out.println(token);
			log.info("开始执行创建素材");
			try {
				String img_name= UUID.randomUUID().toString(); //图片的名称
				DownloadPicFromURLUtil.downloadFromUrl(imgUrl,imagePath+img_name+".jpg");

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
				wxData.put("one_text",content);
				wxData.put("wx_image_url",wx_image_url);
				wxData.put("from",from);
				String oneHtml = CreateHtmlUtils.creteOneHtml("tempalte.ftl", wxData);

				//上传素材到 微信公众号
				WxArticles wxArticles = new WxArticles();
				wxArticles.setTitle(title);
				wxArticles.setContent(oneHtml);
				wxArticles.setAuthor("校园服务");
				wxArticles.setDigest(title);
				wxArticles.setNeed_open_comment(1);
				wxArticles.setOnly_fans_can_comment(0);
				wxArticles.setShow_cover_pic(0);

				//设置素材id
				String forever_url = UPLOAD_FOREVER_MEDIA_URL.replace("ACCESS_TOKEN", token).replace("TYPE", "image");
				File file_f = new File(filename);
				InputStream in_f = new FileInputStream(file_f);
				Map<String,String> forever_map = WxUploadUtil.wxUploadFile(forever_url, in_f,filename);

				wxArticles.setThumb_media_id(forever_map.get("media_id"));
				return CreateArticlesUtils.createArticles(wxArticles,token);

			} catch (Exception e) {
				e.printStackTrace();
		}
		return "";
	}
}
