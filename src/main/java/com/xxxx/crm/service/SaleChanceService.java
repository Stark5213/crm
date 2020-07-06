package com.xxxx.crm.service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.enums.DevResult;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询营销机会数据列表
     */
    public Map<String,Object> selectSaleChanceByParams(SaleChanceQuery saleChanceQuery){
        Map<String,Object> map=new HashMap<>();
        //开启分页
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        PageInfo<SaleChance> pageInfo=new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));

        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 营销机会数据添加
     *      1.参数效验
     *          客户名  非空
     *          联系人  非空
     *          手机号码 非空  格式正确
     *      2.设置参数的默认值
     *          创建时间  createDate  当前时间
     *          更新时间  updateDate   当前时间
     *          是否有效   isValid      1=有效
     *          分配人
     *              默认是未分配
     *                  分配状态    未分配     0=未分配   1=已分配
     *                  开发状态    未开发     0=未开发
     *                  分配时间    空
     *               如果设置分配人
     *                      分配状态    已分配
     *                      开发状态    开发中     0-未开发 1-开发中 2-开发成功  3-开发失败
     *                      分配时间    当前时间
     *       3.执行添加操作
     *          如果失败,抛异常
     */
    public void addSaleChance(SaleChance saleChance){
        //1.参数效验
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //2.设置参数的默认值
        //设置未分配人
        saleChance.setState(StateStatus.UNSTATE.getType());
        saleChance.setDevResult(DevResult.UNDEV.getStatus());

        //如果分配人不为空
        if(StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
            saleChance.setAssignTime(new Date());
        }

        //3.执行添加操作
        AssertUtil.isTrue(insertSelective(saleChance)<1,"营销机会数据添加失败!");


    }

    /**
     * 更新营销机会数据
     *      1. 判断id是否为空 且要修改的数据存在
     *      2. 参数校验
     *          客户名  非空
     *          联系人  非空
     *          手机号码 非空 格式正确
     *      3. 设置默认值
     *          updateDate  当前时间
     *          指派人 如果 未指派 改成 已指派
     *              分配状态    已分配
     *              开发状态    开发中
     *              分配时间    当前时间
     *              分配人      有值
     *          指派人 如果 已指派 改为 未指派
     *              分配状态    未分配
     *              开发状态    未开发
     *              分配时间    null
     *              分配人      空
     *          指派人 如果 未分配  该为 未分配
     分配状态    未分配
     *              开发状态    未开发
     *              分配时间    null
     *              分配人      空
     *          指派人 如果 已分配  改为 已分配
     *              分配状态    已分配
     *              开发状态    开发中
     *              分配时间    当前时间
     *              分配人      有值
     *      4. 执行更新操作
     * @param saleChance
     */
    public void updateSaleChance(SaleChance saleChance){
        //1.判断id是否为空  且要修改的数据存在
        AssertUtil.isTrue(saleChance.getId()==null,"更新失败!");
        //通过id查询营销机会对象
        SaleChance temp=saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(temp==null,"待更新的记录不存在!!");
        //2.参数校验
        checkParams(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());

        //3.设置默认值
        //创建时间在做修改操作不作处理,默认是数据中对应的创建时间
        saleChance.setCreateDate(temp.getCreateDate());
        //更新时间是当前时间
        saleChance.setUpdateDate(new Date());
        //未指派--->已指派
        if(StringUtils.isBlank(temp.getAssignMan())&& StringUtils.isNotBlank(saleChance.getAssignMan())){
            saleChance.setState(StateStatus.STATED.getType());
            saleChance.setDevResult(DevResult.DEVING.getStatus());
            saleChance.setAssignTime(new Date());
        }else if(StringUtils.isNotBlank(temp.getAssignMan()) && StringUtils.isBlank(saleChance.getAssignMan())){
            // 已指派 ————> 未指派
            saleChance.setState(StateStatus.UNSTATE.getType());
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
            saleChance.setAssignTime(null);
            saleChance.setAssignMan("");

        }else if (StringUtils.isNotBlank(saleChance.getAssignMan())&& StringUtils.isNotBlank(temp.getAssignMan())&& !(temp.getAssignMan()).equals(saleChance.getAssignMan())){
            // 已指派 ————> 已指派 （指派前后不是同一个人）
            // 更新指派时间
            saleChance.setAssignTime(new Date());
        }
        //4.执行更新操作
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)<1,"营销机会数据更新失败!");

    }
    /**
     * 参数校验
     *         客户名  非空
     *         联系人  非空
     *         手机号码 非空 格式正确
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(customerName==null,"客户名不能为空!");
        AssertUtil.isTrue(linkMan==null,"联系人不能为空!");
        AssertUtil.isTrue(linkPhone==null,"手机号码不能为空!");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"手机号码格式不正确!");
    }

    /**
     * 查询所有销售人员
     */
    public List<Map<String,Object>> queryAllSales(){
        return saleChanceMapper.queryAllSales();
    }

    /**
     * 营销机会数据删除
     *      1.判断参数是否为空
     *      2.执行删除操作
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteSaleChance(Integer[] ids){
        AssertUtil.isTrue(ids==null,"待删除的记录不存在!");
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids)<1,"营销机会数据删除失败!");
    }










}
