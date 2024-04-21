package com.sch.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sch.common.Result;
import com.sch.entity.Setmeal;
import com.sch.entity.SetmealDto;
import com.sch.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("setmeal")
public class SetMealController {

    @Autowired
    SetmealService setMealService;
    @Autowired
    CacheManager cacheManager;

    @GetMapping("/page")
    public Result<Page> page(int page,int pageSize,String name){
        Page<Setmeal> p=new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.like(name!=null, Setmeal::getName,name);

        setMealService.page(p,queryWrapper);
        return Result.success(p);
    }

    @PostMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public  Result<String> save(@RequestBody SetmealDto setmealDto){
        setMealService.saveWithDish(setmealDto);
        return Result.success("新增成功");
    }
    @GetMapping("/{id}")
    @Cacheable(value = "setmealCache",key = "#id")
    public Result<List<Setmeal>> list(@PathVariable Long id){
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getId,id);
        List<Setmeal> list = setMealService.list(queryWrapper);
        log.info("回写套餐信息----------------------------");
        return Result.success(list);
    }

    @PostMapping("/status/{type}")
    public Result<String> setStatus(@PathVariable Integer type,
                                    @RequestParam Long[] ids){
        for (Long id : ids) {
            setMealService.setStatus(type,id);
        }
        return Result.success("修改状态成功");
    }


    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public Result<String> removeById(@RequestParam("ids") Long[] ids){
        for (Long id : ids) {
            setMealService.removeById(id);
        }
        return Result.success("删除成功");
    }
}
