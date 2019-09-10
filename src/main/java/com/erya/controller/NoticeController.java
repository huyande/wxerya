package com.erya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;



@RestController
@RequestMapping("/notice")
public class NoticeController {

	@Value("${wx.notice.content}")
	private String content;

	@Value("${wx.notice.status}")
	private int status;


	@RequestMapping(value="getnotice",method=RequestMethod.GET)
	public String getnotice(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("data", content);
		jsonObject.put("status", status);
		return jsonObject.toJSONString();
	}
}
