package com.sch.service.serviceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sch.entity.DishFlavor;
import com.sch.mapper.DishFlavorMapper;
import com.sch.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImp extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
