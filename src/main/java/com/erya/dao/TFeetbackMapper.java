package com.erya.dao;

import com.erya.bean.po.TFeetback;

public interface TFeetbackMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TFeetback record);

    int insertSelective(TFeetback record);

    TFeetback selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TFeetback record);

    int updateByPrimaryKey(TFeetback record);
}