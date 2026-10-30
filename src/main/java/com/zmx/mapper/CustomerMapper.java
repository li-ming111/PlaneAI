package com.zmx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zmx.domaim.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
}
