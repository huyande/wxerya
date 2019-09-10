package com.erya.controller;

import com.erya.service.WxArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/art")
public class WxArticleController {

	@Value("${wx.saveToken}")
	private String tokenFilePath;

	@Value("${wx.appid}")
	private String appid;

	@Value("${wx.secret}")
	private String secret;

	@Value("${wx.saveIntnetImagePath}")
	private String imagePath;

	@Autowired
	private WxArticleService wxArticleService;

	//跳转到新增素材文件的页面
	@RequestMapping("/view")
	public String toViewArt(){
		return "index";
	}

	@RequestMapping("create")
	@ResponseBody
	public String createArt(String imgUrl,String content,String title,String from){
		return wxArticleService.createArt(imgUrl,content,title,from,tokenFilePath,appid,secret,imagePath);
		//return "{\"media_id\":\"HOLogY_s_mnxmPpgIiN43VOO3RUbjV-IMOWwWuv9Ip8\"}";
	}

}
