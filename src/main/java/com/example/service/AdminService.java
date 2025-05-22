package com.example.service;

import com.example.entity.Account;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
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
            throw new CustomException("账号或密码错误");
        }
        //比较密码
        if(!account.getPassword().equals(dbAdmin.getPassword())){
            throw new CustomException("账号或密码错误");
        }
        return dbAdmin;
    }
}
