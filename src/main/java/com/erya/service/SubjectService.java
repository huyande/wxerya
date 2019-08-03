package com.erya.service;

import java.util.List;

import com.erya.bean.po.TSubject;
import com.erya.bean.vo.SubjectCount;
import com.github.pagehelper.PageInfo;

public interface SubjectService {

	PageInfo<SubjectCount> searchSuject(Integer currentpage, Integer pagesize, String searchName, String openid, String formid);

	int addSubjectInfo(String name);

	List<TSubject> selectAllSubJect();

}
