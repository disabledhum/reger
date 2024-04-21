package com.sch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sch.entity.Setmeal;
import com.sch.entity.SetmealDto;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    void setStatus(Integer type, Long id);
}
