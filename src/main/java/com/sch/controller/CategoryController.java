package com.sch.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sch.common.Result;
import com.sch.entity.Category;
import com.sch.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;



    @GetMapping("/page")
    public Result<Page<Category>> page(int  page,int pageSize){
        Page<Category> p=new Page<>(page,pageSize);
        categoryService.page(p);
        return Result.success(p);
    }

    @PutMapping
    public Result<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return Result.success("修改成功");
    }

    @DeleteMapping
    public Result<String> removeById(@RequestParam("ids") Long[] ids){
        for (Long id : ids) {
            categoryService.removeById(id);
        }
        return Result.success("删除成功");
    }

    @PostMapping

    public Result<String> save(@RequestBody Category category){
        categoryService.save(category);
        return Result.success("添加成功");
    }

    @GetMapping("/list")
    public Result<List<Category>> list(Category category) {
       /* LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);


        List<Category> list = categoryService.list(queryWrapper);
        return Result.success(list);*/

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return Result.success(list);
    }
}
