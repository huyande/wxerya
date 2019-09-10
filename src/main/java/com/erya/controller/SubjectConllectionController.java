package com.erya.controller;

import com.erya.service.SubjectConllectionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("subject")
public class SubjectConllectionController {
	@Value("${wx.saveToken}")
	private String tokenFilePath;

	@Value("${wx.appid}")
	private String appid;

	@Value("${wx.secret}")
	private String secret;

	@Value("${wx.saveIntnetImagePath}")
	private String imagePath;

	@Resource
	private SubjectConllectionService subjectConllectionService;
	/**
	* @Description
	* @Author  hyd
	* @Date   2019/9/7 0007 下午 11:11
	* @Param
	* @Return
	* @Exception
	*/
	@RequestMapping("creat")
	@ResponseBody
	public String createSubjectArt(String booknum){
		String url_t = "http://www.wu7zhi.com/plugin.php?id=wz_book:book&action=book&bookid="+booknum+"&page=1";
		return subjectConllectionService.createSubjectArt(url_t,tokenFilePath,appid,secret,imagePath);
	}
}
