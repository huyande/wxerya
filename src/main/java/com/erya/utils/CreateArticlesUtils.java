package com.erya.utils;

import com.erya.bean.bo.ArticlesList;
import com.erya.bean.bo.WxArticles;

import java.util.ArrayList;
import java.util.List;

/**
  *  @Author hyd
  *	 @Description : 创建微信图文素材工具类
  *  @Date 2019/8/20 0020
 **/

public class CreateArticlesUtils {
	private static String ADD_ARTICLES_URL= "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=ACCESS_TOKEN";

	public static String createArticles(WxArticles wxArticles,String token){
		ArticlesList articlesList = new ArticlesList();
		List<WxArticles> wxArticleList = new ArrayList<>();
/*
		WxArticles wxArticles = new WxArticles();
		wxArticles.setTitle(title);
		wxArticles.setContent(content);
		wxArticles.setAuthor("校园服务");
		wxArticles.setDigest("简介");
		wxArticles.setNeed_open_comment(1);
		wxArticles.setOnly_fans_can_comment(0);
		wxArticles.setShow_cover_pic(0);
		//设置素材id
		wxArticles.setThumb_media_id("HOLogY_s_mnxmPpgIiN43cyggBGmE9NuYUbs382mmC0");*/

		wxArticleList.add(wxArticles);
		articlesList.setArticles(wxArticleList);
		String jSon = JacksonUtil.toJSon(articlesList);

		String add_rep_url =ADD_ARTICLES_URL.replace("ACCESS_TOKEN", token);
		try {
			String param = HttpClientUtil.httpPost(add_rep_url, jSon);
			System.out.println(param);
			return param;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}


	public static String createArticles(List<WxArticles> wxArticleList,String token){
		ArticlesList articlesList = new ArticlesList();
		articlesList.setArticles(wxArticleList);
		String jSon = JacksonUtil.toJSon(articlesList);

		String add_rep_url =ADD_ARTICLES_URL.replace("ACCESS_TOKEN", token);
		try {
			String param = HttpClientUtil.httpPost(add_rep_url, jSon);
			System.out.println(param);
			return param;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}
}
