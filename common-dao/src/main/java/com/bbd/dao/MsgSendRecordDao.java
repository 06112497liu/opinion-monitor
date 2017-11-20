package com.bbd.dao;

import com.bbd.domain.MsgSendRecord;
import com.bbd.domain.MsgSendRecordExample;
import com.mybatis.domain.PageBounds;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MsgSendRecordDao {
    int deleteByPrimaryKey(Long id);

    int insert(MsgSendRecord record);

    int insertSelective(MsgSendRecord record);

    List<MsgSendRecord> selectByExampleWithPageBounds(MsgSendRecordExample example, PageBounds pageBounds);

    List<MsgSendRecord> selectByExample(MsgSendRecordExample example);

    MsgSendRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MsgSendRecord record, @Param("example") MsgSendRecordExample example);

    int updateByExample(@Param("record") MsgSendRecord record, @Param("example") MsgSendRecordExample example);

    int updateByPrimaryKeySelective(MsgSendRecord record);

    int updateByPrimaryKey(MsgSendRecord record);
}