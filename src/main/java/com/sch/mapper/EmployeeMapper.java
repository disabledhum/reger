package com.sch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sch.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
