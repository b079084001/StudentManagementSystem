package com.example.controller;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.common.RoleEnum;
import com.example.entity.Account;
import com.example.service.AdminService;
import com.example.service.StudentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
public class WebController {
    @Resource
    private AdminService adminService;

    @Resource
    private StudentService studentService;

    /**
     * 默认请求接口
     */
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }

//    @PostMapping("/login")
//    public Result login(@RequestBody Admin admin) {
//        Admin dbAdmin = adminService.login(admin);
//        return Result.success(dbAdmin);
//    }

    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account dbAccount;
        if (RoleEnum.ADMIN.name().equals(account.getRole())) {
            dbAccount = adminService.login(account);
        } else if (RoleEnum.STUDENT.name().equals(account.getRole())) {
            dbAccount = studentService.login(account);
        } else {
            return Result.error("角色错误");
        }

        return Result.success(dbAccount);
    }

    @PostMapping("/register")
    public Result register(@RequestBody Account account) {
        if (ObjectUtil.isEmpty(account.getUsername()) || ObjectUtil.isEmpty(account.getPassword())) {
            return Result.error("账号或密码必填");
        }
        studentService.register(account);
        return Result.success();
    }
}
