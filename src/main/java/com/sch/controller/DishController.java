package com.sch.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sch.common.Result;
import com.sch.dto.DishDto;
import com.sch.entity.Category;
import com.sch.entity.Dish;
import com.sch.entity.DishFlavor;
import com.sch.service.CategoryService;
import com.sch.service.DishFlavorService;
import com.sch.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    DishService dishService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    CacheManager cacheManager;

    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name) {
        log.info("dish查询");
        Page<Dish> p = new Page<>(page, pageSize);
        Page<DishDto> pDto = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(p, queryWrapper);
        BeanUtils.copyProperties(p, pDto, "records");

        List<Dish> records = p.getRecords();
        //
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        pDto.setRecords(list);

        return Result.success(pDto);
    }


    @DeleteMapping
    @CacheEvict(value ="dishCache",allEntries = true)
    public Result<String> removeById(@RequestParam("ids") Long[] ids) {
        log.info("dish删除");
        for (int i = 0; i < ids.length; i++) {
            dishService.removeById(ids[i]);
        }
        return Result.success("删除成功");
    }

    @PutMapping

    public Result<String> update(@RequestBody DishDto dishDto) {
        log.info("dish更新");
        dishService.updateWithFlavor(dishDto);
        return Result.success("更新成功");
    }

    @PostMapping
    @CacheEvict(value ="dishCache",allEntries = true)
    public Result<String> save(@RequestBody DishDto dishDto) {
        log.info("dish新增");
        dishService.saveWithFlavor(dishDto);
        return Result.success("新增成功");
    }

    @GetMapping("/{id}")
    public Result<DishDto> getById(@PathVariable Long id) {
        DishDto dishDto = dishService.getWithFlavor(id);
        return Result.success(dishDto);
    }

    //起售停售控制
    @PostMapping("/status/{type}")
    public Result<String> setStatus(@PathVariable Integer type,
                                    @RequestParam("ids") Long[] ids) {

        for (int i = 0; i < ids.length; i++) {
            dishService.setStatus(type, ids);
        }
        return Result.success("状态更改成功");
    }


    @GetMapping("/list")
    @Cacheable(value = "dishCache",key = "#categoryId")
    public Result<List<DishDto>> saveMeal(@RequestParam("categoryId") Long categoryId) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId != null, Dish::getCategoryId, categoryId);
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByDesc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map(item -> {

                    DishDto dishDto = new DishDto();
                    BeanUtils.copyProperties(item, dishDto);
                    Category category = categoryService.getById(categoryId);

                    if (category != null) {
                        dishDto.setCategoryName(category.getName());
                    }

                    Long id = item.getId();
                    LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                    lambdaQueryWrapper.eq(DishFlavor::getDishId, id);
                    List<DishFlavor> dishFlavors = dishFlavorService.list(lambdaQueryWrapper);
                    dishDto.setFlavors(dishFlavors);
                    return dishDto;
                }
        ).collect(Collectors.toList());

        return Result.success(dishDtoList);
    }
}
