package com.erya.service;

import org.springframework.web.multipart.MultipartFile;

public interface WxArticleService {
	/**
	* @Description 创建素材
	* @Author  hyd
	* @Date   2019/8/23 0023 上午 10:51
	* @Param
	* @Return
	* @Exception
	*/
	String createArt(String imgUrl, String content, String title,String from, String tokenFilePath, String appid, String secret, String imagePath);
}
