package com.sch.service.serviceImp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sch.entity.Orders;
import com.sch.mapper.OrderMapper;
import com.sch.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImp extends ServiceImpl<OrderMapper, Orders>    implements OrderService {
}
