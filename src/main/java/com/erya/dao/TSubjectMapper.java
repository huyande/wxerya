package com.erya.dao;

import java.util.List;

import com.erya.bean.po.TSubject;
import org.apache.ibatis.annotations.Param;



public interface TSubjectMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(TSubject record);

	int insertSelective(TSubject record);

	TSubject selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(TSubject record);

	int updateByPrimaryKey(TSubject record);

	List<TSubject> selectByname(@Param(value="searchName") String searchName);

	int countByName(@Param(value="searchName")String searchName);

	List<TSubject> selectAllSubJect();
}