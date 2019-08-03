package com.erya.dao;

import com.erya.bean.po.TAnswer;
import org.apache.ibatis.annotations.Param;

import java.util.List;




public interface TAnswerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TAnswer record);

    int insertSelective(TAnswer record);

    TAnswer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TAnswer record);

    int updateByPrimaryKey(TAnswer record);

	List<TAnswer> selectByQuestId(Integer questId);

	int deleteByQuestId(@Param(value = "questId") Integer questId);
}