package com.sch.service.serviceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sch.entity.SetmealDish;
import com.sch.mapper.SetmealDishMapper;
import com.sch.service.SetmealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImp extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
