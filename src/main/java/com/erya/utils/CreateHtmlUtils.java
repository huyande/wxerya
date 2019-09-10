package com.erya.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
  *  @Author hyd
  *	 @Description : 通过模板 生成 相应的html 的页面
  *  @Date 2019/8/20 0020
 **/
public class CreateHtmlUtils {
	//发送邮件的模板引擎
	public static String creteOneHtml(String tempName, Map<String,String> data){
		String html ="";
		try {
			Configuration cfg = new Configuration();
			Template template = cfg.getTemplate("temp/"+tempName);
			html = FreeMarkerTemplateUtils.processTemplateIntoString(template,data);
			return html;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html;
	}
	//发送邮件的模板引擎
	public static String creteOneHtml_Ans(String tempName, Map<String,Object> data){
		String html ="";
		try {
			Configuration cfg = new Configuration();
			Template template = cfg.getTemplate("temp/"+tempName);
			html = FreeMarkerTemplateUtils.processTemplateIntoString(template,data);
			return html;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html;
	}
}
