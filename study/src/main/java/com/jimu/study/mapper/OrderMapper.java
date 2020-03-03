package com.jimu.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jimu.study.model.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author hxt
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

    @Select("select order_id, users_id, course_id from orders where order_no = #{orderNo}")
    Orders selectByNo(String orderNo);
}
