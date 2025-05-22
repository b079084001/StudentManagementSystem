package com.example.mapper;
import com.example.entity.Account;
import com.example.entity.Admin;
import org.apache.ibatis.annotations.Select;

public interface AdminMapper {

    @Select("select * from admin where username = #{username}")
    Admin selectByUsername(String username);
    @Select("select * from admin where id = #{id}")
    Account selectById(Integer id);
}
