package com.erya.dao;

import java.util.List;

import com.erya.bean.po.QuestionCount;
import com.erya.bean.po.TQuestion;
import org.apache.ibatis.annotations.Param;


public interface TQuestionMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(TQuestion record);

	int insertSelective(TQuestion record);

	TQuestion selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(TQuestion record);

	int updateByPrimaryKey(TQuestion record);

	List<TQuestion> searchQuestByContentLike(@Param(value="search") String search);

	int searchQuestByContentLikeCount(@Param(value="search")String search);

	List<QuestionCount> searchQuestBySubId();

	int searchQuestBySubIdCount();

	int searchAllQuest();
	//查询改课程id下的问题数
	int countQuestBySubIdCount(@Param(value="subId")Integer subId);

	List<TQuestion> selectQuestBySubId(@Param(value="subId")Integer subId);

	List<TQuestion> selectAllBySubId(@Param(value="subId")Integer id);
}