package com.sch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sch.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
