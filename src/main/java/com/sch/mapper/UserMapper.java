package com.sch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sch.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
