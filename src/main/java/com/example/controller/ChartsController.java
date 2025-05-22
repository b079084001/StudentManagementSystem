package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import com.example.common.Result;
import com.example.entity.Orders;
import com.example.service.OrdersService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@RestController
public class ChartsController {
    @Resource
    OrdersService ordersService;

    @GetMapping("/example")
    public Result get() {
        Map<String, Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y", CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);
    }

    /**
     * 获取统计图数据
     * @return 动态数据
     */
    @GetMapping("/charts")
    public Result charts() {
        // 包装折线图的数据
        List<Orders> list = ordersService.list();
        Set<String> dates = list.stream().map(Orders::getDate).collect(Collectors.toSet());
        List<String> dateList = CollUtil.newArrayList(dates);
        dateList.sort(Comparator.naturalOrder());
        List<Dict> lineList = new ArrayList<>();
        for (String date : dateList) {
            // 统计当前日期的所有金额总数和
            BigDecimal sum = list.stream().filter(orders -> orders.getDate().equals(date)).map(Orders::getMoney)
                    .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            Dict dict = Dict.create();
            Dict line = dict.set("date", date).set("value", sum);
            lineList.add(line);
        }

        // 柱状图数据
        List<Dict> barList = new ArrayList<>();
        Set<String> categories = list.stream().map(Orders::getCategory).collect(Collectors.toSet());
        for (String cate : categories) {
            // 统计当前日期的所有金额总数和
            BigDecimal sum = list.stream().filter(orders -> orders.getCategory().equals(cate)).map(Orders::getMoney)
                    .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            Dict dict = Dict.create();
            Dict bar = dict.set("name", cate).set("value", sum);
            barList.add(bar);
        }

        // 包装所有数据
        Dict res = Dict.create().set("line", lineList).set("bar", barList);
        return Result.success(res);
    }

}
