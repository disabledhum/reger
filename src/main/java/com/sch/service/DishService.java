package com.sch.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sch.dto.DishDto;
import com.sch.entity.Dish;


public interface DishService extends IService<Dish> {
        public void updateWithFlavor(DishDto dishDto);

        public void saveWithFlavor(DishDto dishDto);

        public DishDto getWithFlavor(Long id);

        void setStatus(Integer type, Long[] ids);
}
