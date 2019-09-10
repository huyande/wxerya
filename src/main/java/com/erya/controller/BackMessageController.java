package com.erya.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;
import freemarker.template.TemplateException;



/**
 * 反馈信息
 * @author Administrator
 *
 */
@RestController
@RequestMapping("back")
public class BackMessageController {
	Logger log = LoggerFactory.getLogger(BackMessageController.class);

	@Autowired
    private JavaMailSender mailSender;
	
	//配置邮件发送方 
	@Value("${spring.mail.username}")
    private String Sender; //读取配置文件中的参数
	
	//配置邮件接收方法
	@Value("${backmessage.mail.accept}")
	private String mailAccept; 

	//发送邮件的模板引擎
    @Autowired
    private FreeMarkerConfigurer configurer;
    
	/**
	 * 发送邮件
	 * @param req
	 * @return
	 */
	@RequestMapping("/backmessage")	
	public String backMessage(String openid,String content,String formid){
		log.info("发送反馈信息到邮箱");
		try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(Sender);
            helper.setTo(mailAccept);//发送给谁
            helper.setSubject("来自尔雅搜的问题反馈 ：");//邮件标题

            Map<String, Object> model = new HashMap<>();
            model.put("content", content);
            model.put("openid", openid);
            model.put("formid", formid);
            
            try {
                Template template = configurer.getConfiguration().getTemplate("backmail.ftl");
                try {
                    String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

                    helper.setText(text, true);
                    mailSender.send(mimeMessage);
                } catch (TemplateException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        
        return "{\"result\":true,\"message\":\"反馈成功\"}";
	}

}
