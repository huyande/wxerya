package com.erya.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erya.utils.HttpClientUtil;
import com.erya.utils.JacksonUtil;



/**
 * 用户注册 、保存用户
 * @author Administrator
 *
 */
@RestController
public class LoginController {
	Logger log = LoggerFactory.getLogger(LoginController.class);
	

	@Value("${wx.appid.app}")
	private String appid;
	@Value("${wx.secret.app}")
	private String secret;
	@Value("${wx.grantType.app}")
	private String grantType;
	@Value("${wx.sessionHost.app}")
	private String sessionHost;
	
	/**
	 * 请求微信服务器获取返回的session_key and openid
	 * @param code
	 * @return
	 */
	@RequestMapping("/wxlogin/{code}")
	public String wxlogin(@PathVariable("code") String code){
		log.info("获取session_key and openid");
		// 定义JackJson 对象
		try {
			String api_url =sessionHost+"?appid="+appid+"&secret="+secret+"&js_code="+code+"&grant_type="+grantType ;
			String loginMsg = HttpClientUtil.get(api_url);
			return loginMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
