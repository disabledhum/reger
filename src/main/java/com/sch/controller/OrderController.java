package com.sch.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sch.common.Result;
import com.sch.entity.Orders;
import com.sch.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService    orderService;

    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, Long number, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime,@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime){
        Page<Orders> p=new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(number!=null, Orders::getId,number);
        queryWrapper.between(beginTime!=null&&endTime!=null,Orders::getOrderTime,beginTime,endTime);
        orderService.page(p,queryWrapper);

        return Result.success(p);
    }
}
