package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CusDevPlanMapper;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.CusDevPlan;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan, Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;

    /**
     * 添加计划项
     *      1. 参数校验
     *          计划项
     *          计划时间
     *          计划内容
     *      2. 设置默认值
     *      3. 执行添加操作
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan){
        // 1. 参数校验
        checkParams(cusDevPlan.getExeAffect(), cusDevPlan.getPlanDate(), cusDevPlan.getPlanItem());
        // 设置参数的默认值
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        cusDevPlan.setIsValid(1);
        // 执行添加操作
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan) < 1, "计划项添加失败！");
    }

    /**
     * 参数校验
     * @param exeAffect
     * @param planDate
     * @param planItem
     */
    private void checkParams(String exeAffect, Date planDate, String planItem) {
        AssertUtil.isTrue(StringUtils.isBlank(exeAffect), "计划项不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(planItem), "计划内容不能为空！");
        AssertUtil.isTrue(planDate == null, "计划时间不能为空！");
    }

    /**
     * 添加计划项
     *      1. 参数校验
     *      2. 设置默认值
     *      3. 执行更新操作
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan){
        AssertUtil.isTrue(cusDevPlan.getId() == null, "待更新记录不存在！");
        // TODO 数据库中查询记录是否存在
        // 1. 参数校验
        checkParams(cusDevPlan.getExeAffect(), cusDevPlan.getPlanDate(), cusDevPlan.getPlanItem());
        // 设置参数的默认值
        cusDevPlan.setUpdateDate(new Date());
        // 执行添加操作
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) < 1, "计划项更新失败！");
    }
}
