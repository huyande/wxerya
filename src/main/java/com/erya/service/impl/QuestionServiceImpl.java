package com.erya.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.erya.bean.po.TAnswer;
import com.erya.bean.po.TFeetback;
import com.erya.bean.po.TQuestion;
import com.erya.bean.po.TSubject;
import com.erya.bean.vo.AnswerSet;
import com.erya.bean.po.QuestionCount;
import com.erya.bean.vo.SubjectCount;
import com.erya.dao.TAnswerMapper;
import com.erya.dao.TFeetbackMapper;
import com.erya.dao.TQuestionMapper;
import com.erya.dao.TSubjectMapper;
import com.erya.service.QuestionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class QuestionServiceImpl implements QuestionService {
	
	@Autowired
	private TQuestionMapper questionMapper;
	
	@Autowired
	private TSubjectMapper subjectMapper;
	
	@Autowired
	private TAnswerMapper answerMapper;
	
	@Autowired
	private TFeetbackMapper feetbackMapper;
	

	@Override
	public PageInfo<AnswerSet> searchQuestByContentLike(Integer currentpage, Integer pagesize, String search) {
		int page = currentpage==null?1:currentpage;
		int pageSize = pagesize==null?10:pagesize;
		PageHelper.startPage(page,pageSize);
		//查到所有的问题列表
		List<TQuestion> questList = questionMapper.searchQuestByContentLike(search);
		List<AnswerSet> answerList = new ArrayList<>();
		for(TQuestion quest:questList){
			AnswerSet answerSet = new AnswerSet();
			TSubject subject = subjectMapper.selectByPrimaryKey(quest.getSubId());
			//查询答案集合 
			List<TAnswer> answersList = answerMapper.selectByQuestId(quest.getId());
			List<String> ansStrList = new ArrayList<>();
			StringBuilder anStrsb = new StringBuilder();
			//标识 是否是判断题还是选择题  false 判断题  true 选择题 
			boolean flag =false; 
			for (TAnswer tAnswer : answersList) {
				//如果答案的长度大于1  说明是选择题
				if(tAnswer.getAnswer().length()>1){
					String substring = tAnswer.getAnswer().substring(0, tAnswer.getAnswer().indexOf('.'));
					ansStrList.add(substring);
					flag=true;
				}else{ //其他则是判断题 判断题 无需截取字符串 
					ansStrList.add(tAnswer.getAnswer());
				}
			}
			Collections.sort(ansStrList);
			for (String str : ansStrList) {
				anStrsb.append(str+" ");
			}
			//设置问题
			//answerSet.setQuestion(quest.getContent());
			answerSet.setQuestId(quest.getId());
			answerSet.setQuestion(quest.getOriginalContent());
			answerSet.setSubjectName(subject.getSubName());
			if(flag){
				answerSet.setAnswers(answersList);
			}
			answerSet.setAnswerStr(anStrsb.toString());
			answerList.add(answerSet);
		}
		int count = questionMapper.searchQuestByContentLikeCount(search);
		
		PageInfo<AnswerSet> pageInfo = new PageInfo<>();
		pageInfo.setList(answerList);
		pageInfo.setTotal(count);
		return pageInfo;
	}

	@Override
	public PageInfo<SubjectCount> searchQuestBySubId(Integer currentpage, Integer pagesize) {
		int page = currentpage==null?1:currentpage;
		int pageSize = pagesize==null?100:pagesize;
		PageHelper.startPage(page,pageSize);
		List<QuestionCount>questionCounts = questionMapper.searchQuestBySubId();
		List<SubjectCount> subjectCounts = new ArrayList<>();
		for (QuestionCount questionCount : questionCounts) {
			TSubject subject = subjectMapper.selectByPrimaryKey(questionCount.getSubId());
			SubjectCount subjectCount = new SubjectCount();
			subjectCount.setCount(questionCount.getCount());
			subjectCount.setSubjectName(subject.getSubName());
			subjectCounts.add(subjectCount);
		}
		int count = questionMapper.searchQuestBySubIdCount();
		PageInfo<SubjectCount> pageInfo = new PageInfo<>();
		pageInfo.setList(subjectCounts);
		pageInfo.setTotal(count);
		
		/*for(SubjectCount sub:subjectCounts){
			System.out.println("《"+sub.getSubjectName()+"》"+" "+sub.getCount()+"道题");
		}*/
		return pageInfo;
	}

	
	@Override
	public boolean feetback(String content, String openid,String formid) {
		TFeetback feetback = new TFeetback();
		feetback.setContent(content);
		feetback.setOpenid(openid);
		feetback.setFormId(formid);
		int num = feetbackMapper.insert(feetback);
		return num==1?true:false;
	}


	@Override
	public void countSubjectAndQuest(JSONObject jsonObject) {
		int subjectCount = questionMapper.searchQuestBySubIdCount();
		int questCount = questionMapper.searchAllQuest();
		jsonObject.put("subCount", subjectCount);
		jsonObject.put("questCount", questCount);
	}

	@Override
	public int deleteAllBySubId(Integer subId) {
		List<TQuestion> questList = questionMapper.selectQuestBySubId(subId);
		int num=0;
		for(TQuestion quest :questList){
			questionMapper.deleteByPrimaryKey(quest.getId());
			answerMapper.deleteByQuestId(quest.getId());
			num++;
		}
		return num;
	}

	@Override
	public List<TQuestion> selectAllBySubId(Integer id) {
		List<TQuestion> tqList = questionMapper.selectAllBySubId(id);
		return tqList;
	}

}
