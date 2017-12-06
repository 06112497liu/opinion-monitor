/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.service;

import com.bbd.dao.AccountDao;
import com.bbd.domain.Account;
import com.bbd.domain.AccountExample;
import com.bbd.exception.ApplicationException;
import com.bbd.exception.CommonErrorCode;
import com.bbd.service.param.AccountCreateVO;
import com.bbd.service.param.AccountUpdateVo;
import com.bbd.util.BeanMapperUtil;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.plugin.com.event.COMEventHandler;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author tjwang
 * @version $Id: AccountService.java, v 0.1 2017/9/25 0025 15:09 tjwang Exp $
 */
@Service
public class AccountService {

    @Autowired
    private AccountDao accountDao;

    public Optional<Account> loadByUserId(Long userId) {
        Preconditions.checkNotNull(userId);

        AccountExample exam = new AccountExample();
        exam.createCriteria().andUserIdEqualTo(userId);

        List<Account> ds = accountDao.selectByExample(exam);
        if (ds.size() == 0) {
            return Optional.absent();
        }
        return Optional.of(ds.get(0));
    }

    /**
     * 更新账户
     * @param vo
     */
    public void updateAccount(AccountUpdateVo vo) {
        Date now = new Date();

        Account account = BeanMapperUtil.map(vo, Account.class);
        account.setGmtModified(now);

        accountDao.updateByPrimaryKeySelective(account);
    }

    /**
     * 创建账户
     * @param vo
     */
    public void createAccount(AccountCreateVO vo) {
        Preconditions.checkNotNull(vo, "创建账户参数不能为空");
        vo.validate();
        Date now = new Date();
        Account account = new Account();
        account.setGmtCreate(now);
        BeanUtils.copyProperties(vo, account);
        accountDao.insertSelective(account);
    }

    /**
     * 修改账户信息
     * @param vo
     */
    public void updateAccout(AccountCreateVO vo) {
        Preconditions.checkNotNull(vo, "创建账户参数不能为空");
        vo.validate();

        Date now = new Date();
        Account account = new Account();
        account.setGmtModified(now);
        BeanUtils.copyProperties(vo, account);

        AccountExample example = new AccountExample();
        example.createCriteria().andUserIdEqualTo(vo.getUserId());
        accountDao.updateByExampleSelective(account, example);
    }

    // 更新账户时，校验姓名是否重复
    public void checkUpdateAccountNameExist(String name, Long userId) {
        AccountExample example = new AccountExample();
        example.createCriteria().andUserIdNotEqualTo(userId).andNameEqualTo(name);
        List<Account> list = accountDao.selectByExample(example);
        if(!list.isEmpty()) {
            throw new ApplicationException(CommonErrorCode.PARAM_ERROR, "姓名重复");
        }
    }

    // 创建账户时，校验姓名是否重复
    public void checkCreateAccountNameExist(String name) {
        AccountExample example = new AccountExample();
        example.createCriteria().andNameEqualTo(name);
        List<Account> list = accountDao.selectByExample(example);
        if(!list.isEmpty()) {
            throw new ApplicationException(CommonErrorCode.PARAM_ERROR, "姓名重复");
        }
    }


}
