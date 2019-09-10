package com.erya.service;

import com.erya.bean.vo.AnswerSet;
import com.github.pagehelper.PageInfo;

public interface SolrService {

	//将数据库中的数据 导入到solr中 
	int export(Integer subId) throws Exception;

	/**
	 * solr 搜索方法
	 * @param param
	 * @return
	 */
	PageInfo<AnswerSet> search(String param)throws Exception;
	/**
	 * solr 搜索方法
	 * @param param
	 * @return
	 */
	PageInfo<AnswerSet> search(String param,int rows)throws Exception;

}
