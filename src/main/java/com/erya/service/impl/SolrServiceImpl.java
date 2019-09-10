package com.erya.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.erya.bean.po.TAnswer;
import com.erya.bean.po.TQuestion;
import com.erya.bean.po.TSubject;
import com.erya.bean.vo.AnswerSet;
import com.erya.dao.TAnswerMapper;
import com.erya.dao.TSubjectMapper;
import com.erya.service.QuestionService;
import com.erya.service.SolrService;
import com.erya.service.SubjectService;
import com.erya.utils.JacksonUtil;
import com.github.pagehelper.PageInfo;

@Service
public class SolrServiceImpl implements SolrService {
	@Autowired
	private QuestionService questionService;

	@Autowired
	private SolrClient solrClient;

	@Autowired
	private SubjectService subjectService;

	@Autowired
	private TAnswerMapper answerMapper;
	
	@Autowired
	private TSubjectMapper subjectMapper;

	@Override
	public int export(Integer subId) throws Exception {
		List<AnswerSet> anslist = new ArrayList<>();
		int num=0;
		// 取数据
		if (subId == null) {
			// 查询所有的课程信息
			List<TSubject> subList = subjectService.selectAllSubJect();
			anslist = genderBean(subList);
			num=anslist.size();
		} else {
			List<TQuestion> list = questionService.selectAllBySubId(subId);
			TSubject subject = subjectMapper.selectByPrimaryKey(subId);
			anslist = gender1Bean(list,subject.getSubName());
			num++;
		}
		// end

		// 想solr中 导入数据
		Collection<SolrInputDocument> docList = new ArrayList<SolrInputDocument>();
		for (AnswerSet answerSet : anslist) {
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", answerSet.getQuestId());
			doc.addField("problem", answerSet.getQuestion());
			if(answerSet.getAnswers()!=null){
				List<String> jsonList = new ArrayList<>();
				for (TAnswer tans : answerSet.getAnswers()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id", tans.getId());
					jsonObject.put("answer", tans.getAnswer());
					jsonObject.put("questId", tans.getQuestId());
					jsonList.add(jsonObject.toString());
				}
				doc.addField("answers", jsonList);
			}
			doc.addField("subjectName", answerSet.getSubjectName());
			doc.addField("answerStr", answerSet.getAnswerStr());
			docList.add(doc);
		}
		solrClient.add(docList);
		solrClient.commit();
		return num;
	}

	public List<AnswerSet> genderBean(List<TSubject> subList) {
		List<AnswerSet> anslist = new ArrayList<>();
		// 根据课程信息查询问题信息
		for (TSubject ts : subList) {
			List<TQuestion> list = questionService.selectAllBySubId(ts.getId());
			// 标识 是否是判断题还是选择题 false 判断题 true 选择题
			boolean flag = false;
			for (TQuestion quest : list) {
				StringBuilder anStrsb = new StringBuilder();
				List<String> ansStrList = new ArrayList<>();
				AnswerSet answerSet = new AnswerSet();
				// 查询答案集合
				List<TAnswer> answersList = answerMapper.selectByQuestId(quest.getId());
				for (TAnswer tAnswer : answersList) {
					// 如果答案的长度大于1 说明是选择题
					if (tAnswer.getAnswer().length() >1) {
						String substring;
						try {
							substring = tAnswer.getAnswer().substring(0, tAnswer.getAnswer().indexOf('.'));
							ansStrList.add(substring);
							flag = true;
						} catch (Exception e) {
							System.out.println(tAnswer.getAnswer());
							break;
						}
					} else { // 其他则是判断题 判断题 无需截取字符串
						ansStrList.add(tAnswer.getAnswer());
					}
				}
				Collections.sort(ansStrList);
				for (String str : ansStrList) {
					anStrsb.append(str + " ");
				}
				answerSet.setQuestId(quest.getId());
				answerSet.setQuestion(quest.getOriginalContent());
				answerSet.setSubjectName(ts.getSubName());
				answerSet.setAnswerStr(anStrsb.toString());
				if (flag) {
					answerSet.setAnswers(answersList);
				}

				anslist.add(answerSet);
			}
		}
		
		return anslist;
	}

	public List<AnswerSet> gender1Bean(List<TQuestion> list,String ts) {
		List<AnswerSet> anslist = new ArrayList<>();
		// 标识 是否是判断题还是选择题 false 判断题 true 选择题
		for (TQuestion quest : list) {
			boolean flag = false;
			StringBuilder anStrsb = new StringBuilder();
			List<String> ansStrList = new ArrayList<>();
			AnswerSet answerSet = new AnswerSet();
			// 查询答案集合
			List<TAnswer> answersList = answerMapper.selectByQuestId(quest.getId());
			for (TAnswer tAnswer : answersList) {
				// 如果答案的长度大于1 说明是选择题
				if (tAnswer.getAnswer().length() >1) {
					
					String substring;
					try {
						substring = tAnswer.getAnswer().substring(0, tAnswer.getAnswer().indexOf('.'));
						ansStrList.add(substring);
						flag = true;
					} catch (Exception e) {
						System.out.println(tAnswer.getAnswer());
						break;
					}
				} else { // 其他则是判断题 判断题 无需截取字符串
					ansStrList.add(tAnswer.getAnswer());
				}
			}
			Collections.sort(ansStrList);
			for (String str : ansStrList) {
				anStrsb.append(str + " ");
			}
			answerSet.setQuestId(quest.getId());
			answerSet.setQuestion(quest.getOriginalContent());
			answerSet.setSubjectName(ts);
			answerSet.setAnswerStr(anStrsb.toString());
			if (flag) {
				answerSet.setAnswers(answersList);
			}

			anslist.add(answerSet);
		}
		return anslist;
	}

	@Override
	public PageInfo<AnswerSet> search(String param) throws Exception {
		
		PageInfo<AnswerSet> pageInfo = new PageInfo<>();
		//定义集合
		List<AnswerSet> answerList = new ArrayList<>();
		
		//创建solr 查询对象 封装solr查询条件
		//创建solrquery 对象
		SolrQuery query = new SolrQuery();
		query.setStart(0);
		query.setRows(1);
		query.setQuery("problem:"+param);
		QueryResponse response = solrClient.query(query);
		//获取查询结果
		SolrDocumentList results = response.getResults();
		//获取满足查询条件总记录数
		Long numFound = results.getNumFound();
		for (SolrDocument solrDocument : results) {
			//创建返回数据的对象
			AnswerSet answerSet = new AnswerSet();
			
			//获取文档域id
			String id = (String) solrDocument.get("id");
			String pro =(String) solrDocument.get("problem");
			List<String> answers =(List<String>) solrDocument.get("answers");
			//List<String> answers =(List<String>) JacksonUtil.readValue((String)solrDocument.get("answers"), TAnswer.class);
			String answerStr =(String) solrDocument.get("answerStr");
			String subjectName =(String) solrDocument.get("subjectName");
			
			List<TAnswer> tanList = new ArrayList<>();
			if(answers!=null){
				for(String str :answers){
					TAnswer tAnswer = JacksonUtil.readValue(str, TAnswer.class);
					tanList.add(tAnswer);
				}
			}
			
			//封装数据
			answerSet.setQuestId(Integer.parseInt(id));
			answerSet.setQuestion(pro);
			answerSet.setAnswers(tanList);
			answerSet.setAnswerStr(answerStr);
			answerSet.setSubjectName(subjectName);
			answerList.add(answerSet);
		}
		pageInfo.setList(answerList);
		pageInfo.setTotal(numFound);
		return pageInfo;
	}

	@Override
	public PageInfo<AnswerSet> search(String param, int rows) throws Exception {
		PageInfo<AnswerSet> pageInfo = new PageInfo<>();
		//定义集合
		List<AnswerSet> answerList = new ArrayList<>();

		//创建solr 查询对象 封装solr查询条件
		//创建solrquery 对象
		SolrQuery query = new SolrQuery();
		query.setStart(0);
		query.setRows(rows);
		query.setQuery("problem:"+param);
		QueryResponse response = solrClient.query(query);
		//获取查询结果
		SolrDocumentList results = response.getResults();
		//获取满足查询条件总记录数
		Long numFound = results.getNumFound();
		for (SolrDocument solrDocument : results) {
			//创建返回数据的对象
			AnswerSet answerSet = new AnswerSet();

			//获取文档域id
			String id = (String) solrDocument.get("id");
			String pro =(String) solrDocument.get("problem");
			List<String> answers =(List<String>) solrDocument.get("answers");
			//List<String> answers =(List<String>) JacksonUtil.readValue((String)solrDocument.get("answers"), TAnswer.class);
			String answerStr =(String) solrDocument.get("answerStr");
			String subjectName =(String) solrDocument.get("subjectName");

			List<TAnswer> tanList = new ArrayList<>();
			if(answers!=null){
				for(String str :answers){
					TAnswer tAnswer = JacksonUtil.readValue(str, TAnswer.class);
					tanList.add(tAnswer);
				}
			}

			//封装数据
			answerSet.setQuestId(Integer.parseInt(id));
			answerSet.setQuestion(pro);
			answerSet.setAnswers(tanList);
			answerSet.setAnswerStr(answerStr);
			answerSet.setSubjectName(subjectName);
			answerList.add(answerSet);
		}
		pageInfo.setList(answerList);
		pageInfo.setTotal(numFound);
		return pageInfo;
	}
}
