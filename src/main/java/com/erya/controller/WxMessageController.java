package com.erya.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.erya.bean.bo.WxUserSearch;
import com.erya.utils.ReplyMsgUtils;
import com.erya.utils.WxFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erya.service.WxMessageService;

@RestController
public class WxMessageController {
	
	Logger log = LoggerFactory.getLogger(WxMessageController.class);

	
	@Autowired
	private WxMessageService wxMessageService;
	
	@Value("${wx.number}")
	private String wxNumber;
	
	@Value("${wx.appid}")
	private String appid;
	
	@Value("${wx.secret}")
	private String secret;
	
	@Value("${wx.fileImagePath}")
	private String wXfileImagePath;

	@Value("${adUrl.gotUrl}")
	private String adUrlgotUrl;

	@Value("${adUrl.gotContent}")
	private String adUrlgotContent;

	@Value("${adUrl.notGotUrl}")
	private String adUrlnotGotUrl;

	@Value("${adUrl.notGotContent}")
	private String adUrlnotGotContent;

	@Value("${wx.saveUserFile}")
	private String userSaveFilePath;

	@Value("${wx.openAdLink}")
	private boolean openAdLinkFlag;

	@PostMapping("wx")
	public String message(HttpServletRequest request) {
		Map<String, String> msgXmlMap = wxMessageService.receptionMsg(request);
//		System.out.println(msgXmlMap);
//		WxFileUtils.wirteWxUserToFile(msgXmlMap,true,userSaveFile);
//		List<WxUserSearch> wxUserSearches = (List<WxUserSearch>)WxFileUtils.readWxUserByFile(userSaveFile);
//		System.out.println(wxUserSearches);
		try {
			String xmlMsg = wxMessageService.sendDealMsg(msgXmlMap,wxNumber,appid,secret,wXfileImagePath,adUrlgotUrl,adUrlgotContent,adUrlnotGotUrl,adUrlnotGotContent,userSaveFilePath,openAdLinkFlag);
			//System.out.println(xmlMsg);
			return xmlMsg;
		} catch (Exception e) {
			e.printStackTrace();
			String msg = ReplyMsgUtils.replyTextMsg(msgXmlMap, wxNumber, "系统升级维护中...，请稍后再试！");
			return msg;
		}
	}

	@GetMapping("wxActive")
	public void wxActive(String openid){
		WxFileUtils.wirteWxUserToFile(openid,true,userSaveFilePath);
	}

}
