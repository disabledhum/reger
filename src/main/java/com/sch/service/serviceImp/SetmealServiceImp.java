package com.sch.service.serviceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sch.entity.Setmeal;
import com.sch.entity.SetmealDish;
import com.sch.entity.SetmealDto;
import com.sch.mapper.SetmealMapper;
import com.sch.service.SetmealDishService;
import com.sch.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImp extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    SetmealDishService setmealDishService;
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map(item->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public void setStatus(Integer type, Long id) {
        Setmeal setmeal = this.getById(id);
        if (type==0)    setmeal.setStatus(0);
        else if (type==1)   setmeal.setStatus(1);
        this.updateById(setmeal);
    }
}
