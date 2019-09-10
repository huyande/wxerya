package com.erya.service.impl;

import com.erya.bean.bo.WxArticles;
import com.erya.service.SubjectConllectionService;
import com.erya.utils.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SubjectConllectionServiceImpl implements SubjectConllectionService{
	//上传图文消息中的图片 微信返回 图片url
	private String  UPLOAD_ARTICLE_URL = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN";

	//上传永久素材 返回mediaId
	private String UPLOAD_FOREVER_MEDIA_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";

	@Override
	public String createSubjectArt(String url,String tokenFilePath, String appid, String secret, String imagePath) {
		String token = WxAccessTokenUtil.getAccessToken(tokenFilePath,appid,secret);
		System.out.println(token);
		try {
		//发送请求 获取到网页信息  拿到相关书籍的信息
		String html = HttpClientUtil.get(url);
		Map<String, Object> subjectMap = JsoupUtils.parserHtmlSubject(html);
		//获取答案集合
		List<Map<String,String>> ansInfoMap = (List<Map<String,String>>)subjectMap.get("ansInfo");
		//创建以章节为key  图片返回url 集合的map
		Map<String,List<String>> wxAnsImgMap = new HashMap<>();

		for(Map<String,String> map :ansInfoMap){
			for(Map.Entry<String,String> m:map.entrySet()){
				String anshtml = HttpClientUtil.get(m.getValue());
				List<String> imagesList = JsoupUtils.parserHtmlForImage(anshtml);
				int index =0;
				List<String> wxImgUrl = new ArrayList<>();

				for(String imgUrl:imagesList){
					//拼装路径
					String book =subjectMap.get("bookName").toString().replaceAll( "[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]" , "").replaceAll(" ","");
					String zhangjie = m.getKey().replaceAll( "[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]" , "").replaceAll(" ","");
					DownloadPicFromURLUtil.downloadFromUrl(imgUrl,imagePath+book+"/"+zhangjie+"/"+index+".jpg");
					//上传照片 获取图片的url
					String filename = imagePath+"/"+book+"/"+zhangjie+"/"+index+".jpg";
					File file = new File(filename);
					InputStream in = new FileInputStream(file);
					String replaceArt_url =UPLOAD_ARTICLE_URL.replace("ACCESS_TOKEN",token);
					Map<String,String> mapParam = WxUploadUtil.wxUploadFile(replaceArt_url, in,filename);
					//获取到上传到微信服务器 返回过来的url
					String wx_image_url = mapParam.get("url");
					wxImgUrl.add(wx_image_url);
					index++;
				}
				wxAnsImgMap.put(m.getKey(),wxImgUrl);
			}
		}
		//上传素材 并且分类 end
			String forever_url = UPLOAD_FOREVER_MEDIA_URL.replace("ACCESS_TOKEN", token).replace("TYPE", "image");
			//拼装路径
			DownloadPicFromURLUtil.downloadFromUrl(subjectMap.get("img").toString(),imagePath+"00.jpg");
			//上传照片 获取图片的url
			String filename_1 = imagePath+"00.jpg";
			File file_f = new File(filename_1);
			InputStream in_f = new FileInputStream(file_f);
			Map<String,String> forever_map = WxUploadUtil.wxUploadFile(forever_url, in_f,filename_1);

			File file_b = new File(filename_1);
			InputStream in_b = new FileInputStream(file_b);
			String replaceArt_url_b =UPLOAD_ARTICLE_URL.replace("ACCESS_TOKEN",token);
			Map<String,String> mapParam = WxUploadUtil.wxUploadFile(replaceArt_url_b, in_b,filename_1);
			String bookImg = mapParam.get("url");

			//创建素材集合
			List<WxArticles> wxArticleList = new ArrayList<>();
			for(Map.Entry<String,List<String>> mm:wxAnsImgMap.entrySet()){
				//生成html 字符串
				Map<String,Object> wxData =  new HashMap<>();
				wxData.put("bookImg",bookImg);
				wxData.put("bookName",subjectMap.get("bookName"));
				wxData.put("author",subjectMap.get("author"));
				wxData.put("press",subjectMap.get("press"));
				wxData.put("one_text",mm.getKey());
				wxData.put("wx_image_urls",mm.getValue());
				String oneHtml = CreateHtmlUtils.creteOneHtml_Ans("tempAns.ftl", wxData);

				WxArticles wxArticles = new WxArticles();
				wxArticles.setThumb_media_id(forever_map.get("media_id"));
				wxArticles.setTitle(mm.getKey());
				wxArticles.setContent(oneHtml);
				wxArticles.setAuthor("校园服务");
				wxArticles.setDigest(mm.getKey());
				wxArticles.setNeed_open_comment(1);
				wxArticles.setOnly_fans_can_comment(0);
				wxArticles.setShow_cover_pic(0);

				wxArticleList.add(wxArticles);
			}

			return CreateArticlesUtils.createArticles(wxArticleList,token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
