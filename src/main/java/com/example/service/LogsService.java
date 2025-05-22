package com.example.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Logs;
import com.example.mapper.LogsMapper;
import org.springframework.stereotype.Service;

@Service
public class LogsService extends ServiceImpl<LogsMapper, Logs> {

}
