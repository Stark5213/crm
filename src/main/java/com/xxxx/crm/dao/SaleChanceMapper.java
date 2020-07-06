package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.SaleChance;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.List;
import java.util.Map;

public interface SaleChanceMapper extends BaseMapper<SaleChance, Integer> {

    //查询所有销售人员
    public List<Map<String,Object>> queryAllSales();
}