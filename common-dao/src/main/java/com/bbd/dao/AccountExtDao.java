package com.bbd.dao;

import com.bbd.domain.Account;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Liuweibo
 * @version Id: AccountExtDao.java, v0.1 2017/12/18 Liuweibo Exp $$
 */
public interface AccountExtDao {

    List<Account> getTransferList(@Param(value = "id") Long id, @Param(value = "region") String region);

}
    
    