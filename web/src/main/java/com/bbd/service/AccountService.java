/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.dao.AccountDao;
import com.bbd.domain.Account;
import com.bbd.domain.AccountExample;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author tjwang
 * @version $Id: AccountService.java, v 0.1 2017/9/25 0025 15:09 tjwang Exp $
 */
@Service
public class AccountService {

    @Autowired
    private AccountDao accountDao;

    public Optional<Account> queryByUserId(Long userId) {
        Preconditions.checkNotNull(userId);

        AccountExample exam = new AccountExample();
        exam.createCriteria().andUserIdEqualTo(userId);

        List<Account> ds = accountDao.selectByExample(exam);
        if (ds.size() == 0) {
            return Optional.absent();
        }
        return Optional.of(ds.get(0));
    }
}
