package com.example.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.exception.ServiceException;
import com.example.service.UserService;
import com.example.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.rmi.ServerException;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 新增用户信息
     */
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        try {
            userService.save(user);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                return Result.error(ResultCodeEnum.valueOf("插入数据库错误"));
            } else {
                return Result.error(ResultCodeEnum.valueOf("系统错误"));
            }
        }
        return Result.success();
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        userService.updateById(user);
        return Result.success();
    }

    /**
     * 删除用户信息
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (id.equals(currentUser.getId())) {
            throw new ServiceException("不能删除当前的用户");
        }
        userService.removeById(id);
        return Result.success();
    }


    /**
     * 批量删除用户信息
     */
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {  //  [7, 8]
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser != null && currentUser.getId() != null && ids.contains(currentUser.getId())) {
            throw new ServiceException("不能删除当前的用户");
        }
        userService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 查询全部用户信息
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        List<User> userList = userService.list(new QueryWrapper<User>().orderByDesc("id"));  // select * from user order by id desc
        return Result.success(userList);
    }

    /**
     * 根据ID查询用户信息
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }


    /**
     * 多条件模糊查询用户信息
     * pageNum 当前的页码
     * pageSize 每页查询的个数
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String username,
                               @RequestParam String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().orderByDesc("id");  // 默认倒序，让最新的数据在最上面
        queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        // select * from user where username like '%#{username}%' and name like '%#{name}%'
        Page<User> page = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        return Result.success(page);
    }

    /**
     * 批量导出数据(方式1)
     */
//    @GetMapping("/export")
//    public void exportData(@RequestParam(required = false) String username,
//                           @RequestParam(required = false) String name,
//                           @RequestParam(required = false) String ids,  //   1,2,3,4,5
//                           HttpServletResponse response) throws IOException {
//        ExcelWriter writer = ExcelUtil.getWriter(true);
//
//        List<User> list;
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        if (StrUtil.isNotBlank(ids)) {     // ["1", "2", "3"]   => [1,2,3]
//            List<Integer> idsArr1 = Arrays.stream(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
//            queryWrapper.in("id", idsArr1);
//        } else {
//            // 第一种全部导出或者条件导出
//            queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
//            queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
//        }
//        list = userService.list(queryWrapper);   // 查询出当前User表的所有数据
//        writer.write(list, true);
//
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
//        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("用户信息表", "UTF-8") + ".xlsx");
//        ServletOutputStream outputStream = response.getOutputStream();
//        writer.flush(outputStream, true);
//        writer.close();
//        outputStream.flush();
//        outputStream.close();
//
//    }

    /**
     * 批量导出数据(方式2)
     */
    @GetMapping("/export")
    public void exportData(HttpServletResponse response) throws IOException {
        //一行一行的组装数据，塞到List中
        //每行数据，对应数据库表的一行数据，对应Java的一个实体类
        //1.从数据库中查询所有数据
        List<Account> all = userService.findAll();
        if (CollectionUtil.isEmpty(all)) {
            throw new ServerException("未找到数据");
        }

        //2.定义一个List和map<key,value>,存储处理后的数据，用于塞到List
        List<Map<String, Object>> lst = new ArrayList<>(all.size());

        //3.遍历每一条数据，封装到map<key,value>，把map塞到List
        for (Account user : all) {
            Map<String, Object> row = new HashMap<>();
            row.put("序号", user.getId());
            row.put("用户名", user.getUsername());
            lst.add(row);
        }
        //4.创建一个ExcelWriter对象，把List数据用write出来
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.write(lst, true);

        //5.把excel下载下来
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("用户信息表", "UTF-8") + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        writer.close();
        IoUtil.close(System.out);
    }

    /**
     * 批量导入
     *
     * @param file 传入的excel文件对象
     * @return 导入结果
     */
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<User> userList = reader.readAll(User.class);
        // 写入数据到数据库
        try {
            userService.saveBatch(userList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(ResultCodeEnum.valueOf("数据批量导入错误"));
        }
        return Result.success();
    }

}
