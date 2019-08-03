package com.erya.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erya.bean.po.TFeetback;
import com.erya.bean.po.TSubject;
import com.erya.bean.vo.SubjectCount;
import com.erya.dao.TFeetbackMapper;
import com.erya.dao.TQuestionMapper;
import com.erya.dao.TSubjectMapper;
import com.erya.service.SubjectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
@Service
public class SubjectServiceImpl implements SubjectService {
	@Autowired
	private TSubjectMapper subjectMapper;
	
	@Autowired
	private TQuestionMapper questionMapper;
	
	@Autowired
	private TFeetbackMapper feetbackMapper;

	@Override
	public PageInfo<SubjectCount> searchSuject(Integer currentpage, Integer pagesize, String searchName,
			String openid,String formid) {
		int page = currentpage==null?1:currentpage;
		int pageSize = pagesize==null?10:pagesize;
		PageHelper.startPage(page,pageSize);
		List<TSubject> subjectList = subjectMapper.selectByname(searchName);
		//如果没有查到 记录到系统反馈信息里 
		if(subjectList.size()==0){ //暂时没有对反馈信息 做去重处理 
			TFeetback feetback = new TFeetback();
			feetback.setContent(searchName);
			feetback.setOpenid(openid);
			feetback.setFormId(formid);
			feetbackMapper.insert(feetback);
		}
		List<SubjectCount> subjectCounts = new ArrayList<>();
		for(TSubject subject : subjectList){
			int count = questionMapper.countQuestBySubIdCount(subject.getId());
			SubjectCount subjectCount = new SubjectCount();
			subjectCount.setCount(count);
			subjectCount.setSubjectName(subject.getSubName());
			subjectCounts.add(subjectCount);
		}
		int count = subjectMapper.countByName(searchName);
		PageInfo<SubjectCount> pageInfo = new PageInfo<>();
		pageInfo.setList(subjectCounts);
		pageInfo.setTotal(count);
		return pageInfo;
	}

	@Override
	public int addSubjectInfo(String name) {
		List<TSubject> selectByname = subjectMapper.selectByname(name);
		if(selectByname!=null && selectByname.size()>0){
			return -1;
		}else{
			TSubject subject = new TSubject();
			subject.setSubName(name);
			subjectMapper.insert(subject);
			return subject.getId();
		}
	}

	@Override
	public List<TSubject> selectAllSubJect() {
		List<TSubject> list = subjectMapper.selectAllSubJect();
		return list;
	}

}
