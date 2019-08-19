package com.erya.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.erya.bean.vo.AnswerSet;
import com.erya.service.SolrService;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/solr")
public class SolrSearchController {
	Logger log = LoggerFactory.getLogger(SolrSearchController.class);

	@Autowired
	private SolrService solrService;
	
	@RequestMapping("/search")
	public String search(String param){
		log.info("搜索内容："+param.replaceAll( "[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]" , "").replaceAll("\\(|\\)", ""));
		PageInfo<AnswerSet> pageInfo=null;
		try {
			pageInfo = solrService.search(param.replaceAll( "[\\p{P}+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]" , "").replaceAll("\\(|\\)", ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("data", pageInfo.getList());
		jsonObject.put("total", pageInfo.getTotal());
		return jsonObject.toJSONString();
	}
	
	@RequestMapping("export")
	public String export(Integer subId){
		log.info("向solr导入");
		int num= 0;
		int j =0;
		try {
			num = solrService.export(subId);
			/*int [] a ={1,2,3,4,5,6,7,8,9,10};
			for(int i=0;i<a.length;i++){
				num = solrService.export(a[i]);
				System.out.println(j);
				j++;
				Thread.sleep(5000);
			}*/
			System.out.println("完成");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ""+num+"";
	}
}
