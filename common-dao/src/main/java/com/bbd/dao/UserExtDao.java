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
     * @param isAdmin
     * @param region
     * @param pb
     * @return
     */
    List<UserListVO> queryUserListLimt(@Param("isAdmin") Boolean isAdmin, @Param("region") String region, PageBounds pb);

    /**
     * 转发用户列表
     * @param region
     * @return
     */
    List<UserListVO> queryUserList(@Param("region") String region);


}