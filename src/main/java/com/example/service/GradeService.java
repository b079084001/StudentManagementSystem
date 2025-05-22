package com.example.service;

import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Account;
import com.example.entity.Grade;
import com.example.exception.CustomException;
import com.example.mapper.GradeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import com.example.common.enums.RoleEnum;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GradeService {
    @Resource
    private GradeMapper gradeMapper;

    public void insert(Grade grade) {
        Grade dbGrade = gradeMapper.selectByCondition(grade);
        if (dbGrade != null) { //打过分了
            throw new CustomException(ResultCodeEnum.SCORE_ALREADY_ERROR);
        }
        gradeMapper.insert(grade);
    }

    public void update(Grade grade) {
        gradeMapper.update(grade);
    }

    public PageInfo<Grade> selectPage(Integer pageNum, Integer pageSize, Grade grade) {
        PageHelper.startPage(pageNum, pageSize);
        List<Grade> list = gradeMapper.selectAll(grade);
        return PageInfo.of(list);
    }


    public void deleteById(Integer id) {
        gradeMapper.deleteById(id);
    }
}
