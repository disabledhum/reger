package com.sch.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sch.common.BaseContext;
import com.sch.common.Result;
import com.sch.entity.Employee;
import com.sch.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpRequest;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    CacheManager cacheManager;


    @PostMapping("/login")
    public Result<Employee> login( HttpServletRequest request, @RequestBody Employee employee){
        //md5加密
        String password = employee.getPassword();
         password = DigestUtils.md5DigestAsHex(password.getBytes());
         //通过用户名查询是否存在用户
         LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
         queryWrapper.eq(Employee::getUsername,employee.getUsername());
         Employee emp = employeeService.getOne(queryWrapper);
        //判断该用户名是否正确
         if (emp==null)  return Result.error("用户名错误");
        //查询用户密码是否正确   查看状态是否被禁用
        if (!emp.getPassword().equals(password))        return Result.error("密码错误");
        if (emp.getStatus()==0)     return Result.error("被禁用");

        request.getSession().setAttribute("employee",emp.getId());


        return Result.success(emp);
    }
@PostMapping("/logout")
    public Result<String> logOut(HttpServletRequest httpServletRequest){
    httpServletRequest.getSession().removeAttribute("employee");
    return Result.success("登出成功");
    }

    @GetMapping("/page")
    public Result<Page> page(int page,int pageSize,String name){
        Page<Employee> p=new Page<>(page,pageSize);
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName,name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(p,queryWrapper);
        return  Result.success(p);
    }

    @PostMapping
    @CachePut(value ="Employee1",key = "#employee.name")
    public Result<String> save(@RequestBody Employee employee){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employeeService.save(employee);
        return Result.success("新增员工成功");
    }

    @PutMapping
    public Result<String> update(@RequestBody Employee employee){
        log.info("更新员工");
        employeeService.updateById(employee);
        return Result.success("更新员工成功");
    }

    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id){
        log.info("回写页面信息   "+id);
        Employee employee = employeeService.getById(id);
        if (employee!=null){
            return Result.success(employee);
        }
        return Result.error("没有该员工");
    }
}
