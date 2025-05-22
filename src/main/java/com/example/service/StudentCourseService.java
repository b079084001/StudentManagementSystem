package com.example.service;


import com.example.common.enums.ResultCodeEnum;
import com.example.entity.StudentCourse;
import com.example.exception.CustomException;
import com.example.mapper.StudentCourseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentCourseService {
    @Resource
    private StudentCourseMapper studentCourseMapper;

    public void add(StudentCourse studentCourse) {
        StudentCourse course = studentCourseMapper.selectByCondition(studentCourse); //看下学生之前有无选择
        if (course != null) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        studentCourseMapper.insert(studentCourse);
    }

    public PageInfo<StudentCourse> selectPage(Integer pageNum, Integer pageSize, StudentCourse studentCourse) {
        PageHelper.startPage(pageNum, pageSize);
        List<StudentCourse> list = studentCourseMapper.selectAll(studentCourse);
        return PageInfo.of(list);
    }

    public void deleteById(Integer id) {
        studentCourseMapper.deleteById(id);
    }
}
