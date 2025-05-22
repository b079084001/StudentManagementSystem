package com.example.service;

import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import com.example.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminService {
    @Resource
    private AdminMapper adminMapper;

//    public Admin login(Account admin) {
//        Admin dbAdmin = adminMapper.selectByUsername(admin.getUsername());
//        //没有用户
//        if (dbAdmin == null) {
//            throw new CustomException("账号或密码错误");
//        }
//        //比较密码
//        if(!admin.getPassword().equals(dbAdmin.getPassword())){
//            throw new CustomException("账号或密码错误");
//        }
//        return dbAdmin;
//    }

    public Account login(Account account) {
        Account dbAdmin = adminMapper.selectByUsername(account.getUsername());
        //没有用户
        if (dbAdmin == null) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        //比较密码
        if(!account.getPassword().equals(dbAdmin.getPassword())){
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String tokenData = dbAdmin.getId() + "-" + RoleEnum.ADMIN.name();
        String token = TokenUtils.createToken(tokenData, dbAdmin.getPassword());
        dbAdmin.setToken(token);
        return dbAdmin;
    }

    public Account selectById(Integer id) {
        return adminMapper.selectById(id);
    }
}
