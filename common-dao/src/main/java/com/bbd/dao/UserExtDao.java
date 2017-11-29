package com.bbd.dao;

import com.bbd.bean.UserListVO;
import com.bbd.domain.User;
import com.bbd.domain.UserExample;
import com.mybatis.domain.PageBounds;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserExtDao {
    /**
     * 用户列表
     * @return
     */
    List<UserListVO> queryUserList(@Param("region") String region, PageBounds pb);

    /**
     * 转发用户列表
     * @return
     */
    List<UserListVO> queryTransUserList();


}