package com.jimu.study.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jimu.study.model.VipOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author hxt
 */
@Mapper
public interface VipOrderMapper extends BaseMapper<VipOrder> {
}
