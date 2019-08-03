package com.erya.dao;

import com.erya.bean.po.TAdurl;

public interface TAdurlMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TAdurl record);

    int insertSelective(TAdurl record);

    TAdurl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TAdurl record);

    int updateByPrimaryKey(TAdurl record);
}