package com.erya.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsoupUtils {

	public static Map<String,String> parserHtml(String html){
		Document doc = Jsoup.parse(html);
		Elements section = doc.select("#carousel-one");
		Document sectionDoc = Jsoup.parse(section.toString());
		Elements div = sectionDoc.getElementsByClass("carousel-inner");
		Document div_caro = Jsoup.parse(div.toString());
		Elements div_item = sectionDoc.getElementsByClass("item active");
		Document div_caro_item = Jsoup.parse(div_item.toString());
		//拿到文字所在的 div 标签
		Elements div_a = div_caro_item.getElementsByClass("fp-one-cita");
		Document div_a_doc = Jsoup.parse(div_a.toString());
		//拿到文字所在的a 标签
		Elements a_div = div_a_doc.select("a");
		//拿到图片标签
		Elements div_img = div_caro_item.getElementsByClass("fp-one-imagen");

		String key ="";
		String value = "";
		for(Element img:div_img){
			key = img.attr("src");
			System.out.println(img.attr("src"));
		}
		for(Element a_cita:a_div){
			value = a_cita.text();
			System.out.println(a_cita.text());
		}
		Map<String,String> data = new HashMap<>();
		data.put(key,value);
		return data;
	}


	public static List<String>  parserHtmlForImage(String html){
		Document doc = Jsoup.parse(html);
		Elements section = doc.select("#js_content");
		Document sectionDoc = Jsoup.parse(section.toString());
		Elements sectionDocByP = sectionDoc.select("p");
		Document Powered_by_Doc = Jsoup.parse(sectionDocByP.toString());
		Elements p_list = Powered_by_Doc.select("img");
		List<String> urlList = new ArrayList<>();
		for(Element e :p_list){
			if(e.attr("data-src").contains("TAoksPVlXMI7dQPxiaUbAHvyJ19iaG9b2Ueh53iaqTsn6F8O3m63zcBibgNpujM1HNeCKX99vOov72LpHuqs92SMlg")){
				continue;
			}
			urlList.add(e.attr("data-src"));
		}
		return urlList;
	}

	/**
	* @Description 解析获取课程答案信息
	* @Author  hyd
	* @Date   2019/9/8 0008 下午 9:49
	* @Param
	* @Return
	* @Exception
	*/
	public static Map<String,Object> parserHtmlSubject(String html){
		Document doc = Jsoup.parse(html);
		Elements section = doc.getElementsByClass("ub ub-ver");
		Document sectionDoc = Jsoup.parse(section.toString());
		//拿到书籍相关信息
		Elements sectionDocBookInfo = sectionDoc.getElementsByClass("ub ub-img1 icon uba sc-border-tab");
		Map<String, Object> mapData = new HashMap<>();
		for(Element e :sectionDocBookInfo){
			mapData.put("img", "http://www.wu7zhi.com"+e.attr("style").substring(+e.attr("style").indexOf(".")+1, e.attr("style").lastIndexOf(")")));
		}
		//拿书籍其他的信息
		Elements bookInfo = sectionDoc.getElementsByClass("ub ub-f1 ub-pc ub-ver umar-l06 umar-t04");
		for(Element e:bookInfo){
			System.out.println(e.text());
			mapData.put("bookName", e.text().substring(0,e.text().indexOf("作者：")));
			mapData.put("author", e.text().substring(e.text().indexOf("作者：")+3,e.text().indexOf("出版社：")));
			mapData.put("press", e.text().substring(e.text().indexOf("出版社：")+4,e.text().length()));
		}

		//拿答案链接信息
		Elements answerElements = sectionDoc.getElementsByClass("ub ub-ver ubt sc-border-active umar-t04 umar-b12 uinn-a06");
		Document answerDoc = Jsoup.parse(answerElements.toString());
		Elements answerA = answerDoc.select("a");
		List<Map<String,String>> ansUrlList = new ArrayList<>();
		for(Element e :answerA){
			Map<String,String> ansMap = new HashMap<>();
			ansMap.put(e.text(), e.attr("href"));
			ansUrlList.add(ansMap);
		}
		mapData.put("ansInfo", ansUrlList);
		return mapData;
	}


}
