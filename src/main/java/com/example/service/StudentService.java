package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Student;
import com.example.exception.CustomException;
import com.example.mapper.StudentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentService {

    @Resource
    private StudentMapper studentMapper;

    public Account login(Account account) {
        Account dbStudent = studentMapper.selectByUsername(account.getUsername());
        //没有用户
        if (dbStudent == null) {
            throw new CustomException("账号或密码错误");
        }
        //比较密码
        if (!account.getPassword().equals(dbStudent.getPassword())) {
            throw new CustomException("账号或密码错误");
        }
        return dbStudent;
    }

    public void register(Account account) {
        Student student = new Student();
        student.setUsername(account.getUsername());
        student.setPassword(account.getPassword());
        this.add(student);
    }

    public void add(Student student) {
        Student dbStudent = studentMapper.selectByUsername(student.getUsername());
        if (dbStudent != null) {
            throw new CustomException("账号已存在");
        }
        if (ObjectUtil.isEmpty(student.getName())) {
            student.setName(student.getUsername());
        }
        student.setRole(RoleEnum.STUDENT.name());
        studentMapper.insert(student);
    }

    public void deleteById(Integer id) {
        studentMapper.deleteById(id);
    }

    public void updateById(Student student) {
        studentMapper.updateById(student);
    }

    public PageInfo<Student> selectPage(Integer pageNum, Integer pageSize, Student student) {
        PageHelper.startPage(pageNum, pageSize);
        List<Student> studentList = studentMapper.selectAll(student);
        return PageInfo.of(studentList);
    }
}
