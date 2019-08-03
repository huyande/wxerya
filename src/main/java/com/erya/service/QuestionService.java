package com.erya.service;

import java.io.File;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.erya.bean.po.TQuestion;
import com.erya.bean.vo.AnswerSet;
import com.erya.bean.vo.SubjectCount;
import com.github.pagehelper.PageInfo;

public interface QuestionService {
	/**
	 * 分页查询 问题答案
	 * @param currentpage
	 * @param pagesize
	 * @param search
	 * @return
	 */
	PageInfo<AnswerSet> searchQuestByContentLike(Integer currentpage, Integer pagesize, String search);
	/**
	 * 统计相关课程的收录问题数
	 * @param currentpage
	 * @param pagesize
	 * @return
	 */
	PageInfo<SubjectCount> searchQuestBySubId(Integer currentpage, Integer pagesize);
	/**
	 * 反馈课程信息
	 * @param content
	 * @param openid
	 * @return
	 */
	boolean feetback(String content, String openid, String formid);
	/**
	 * 统计课程总数和问题答案总数
	 * @param jsonObject
	 */
	void countSubjectAndQuest(JSONObject jsonObject);
	
	/**
	 * 删除该课程下的所有问题和答案
	 * @param subId
	 * @return
	 */
	int deleteAllBySubId(Integer subId);
	/**
	 * 根据课程id 查询所有的问题
	 * @param id
	 * @return
	 */
	List<TQuestion> selectAllBySubId(Integer id);

}
