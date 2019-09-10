package com.erya.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import com.erya.bean.bo.WxUserSearch;
import com.erya.utils.ReplyMsgUtils;
import com.erya.utils.WxFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erya.service.WxMessageService;

@RestController
public class WxMessageController {
	
	Logger log = LoggerFactory.getLogger(WxMessageController.class);

	
	@Autowired
	private WxMessageService wxMessageService;

	@Autowired
	private JavaMailSender mailSender;

	//配置邮件发送方
	@Value("${spring.mail.username}")
	private String Sender; //读取配置文件中的参数

	//配置邮件接收方法
	@Value("${backmessage.mail.accept}")
	private String mailAccept;


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
			String msg = ReplyMsgUtils.replyTextMsg(msgXmlMap, wxNumber, "系统升级维护中，请晚点在来，我在更新系统，感谢使用！");
			try {
				MimeMessage mimeMessage = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
				helper.setFrom(Sender);
				helper.setTo(mailAccept);//发送给谁
				helper.setSubject("系统出现异常，请查看 ");//邮件标题
				helper.setText("公众号出现问题，快快去查看", true);
				mailSender.send(mimeMessage);
			} catch (MessagingException ex) {
				ex.printStackTrace();
			}
			return msg;
		}
	}

	@GetMapping("wxActive")
	public void wxActive(String openid){
		WxFileUtils.wirteWxUserToFile(openid,true,userSaveFilePath);
	}

}
