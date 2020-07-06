package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private UserService userService;
    /**
     * 多条件查询营销机会
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> selectSaleChanceByParam(SaleChanceQuery query,Integer flag,HttpServletRequest request){
       //通过flag的值来判断当前的操作是查询营销机会数据还是客户开发计划数据
        if(flag !=null && flag ==1){
            //查询客户开发计划数据(查询已分配当前登录用户为指派人的营销机会数据)
            //得到当前登录用户的ID
            Integer userId=LoginUserUtil.releaseUserIdFromCookie(request);
            //设置指派人
            query.setAssignMan(userId);

        }



        return saleChanceService.selectSaleChanceByParams(query);
    }

    /**
     * 进入营销机会页面
     */
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";

    }
    /**
     * 营销机会数据添加
     * @param saleChance
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addsaleChance(SaleChance saleChance, HttpServletRequest request){
        //获取用户ID
        Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
        //获取用户的真实姓名
        String trueName= userService.selectByPrimaryKey(userId).getTrueName();
        //将真实姓名设置为创建人
        saleChance.setCreateMan(trueName);
        //调用Service层的添加方法
        saleChanceService.addSaleChance(saleChance);
        return success("success");
    }

    /**
     * 营销机会数据添加
     * @param saleChance
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updatesaleChance(SaleChance saleChance, HttpServletRequest request){
        //获取用户ID
        Integer userId= LoginUserUtil.releaseUserIdFromCookie(request);
        //获取用户的真实姓名
        String trueName= userService.selectByPrimaryKey(userId).getTrueName();
        //将真实姓名设置为创建人
        saleChance.setCreateMan(trueName);
        //调用Service层的添加方法
        saleChanceService.updateSaleChance(saleChance);
        return success("success");
    }
    /**
     * 机会数据添加与更新页面视图转发
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateSaleChancePage")
    public String addOrUpdateSaleChancePage(Integer id, Model model){

        //判断id是否为空  不为空查询指定id的数据
        if(id!=null && id!=0){
            //通过id查询营销机会对象
            SaleChance saleChance=saleChanceService.selectByPrimaryKey(id);
            //设置数据到请求域中
            model.addAttribute("saleChance",saleChance);

        }


        return "saleChance/add_update";
    }

    /**
     * 查询所有销售人员
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String, Object>> queryAllSales(){
        return saleChanceService.queryAllSales();
    }


    /**
     * 营销机会数据删除
     */
    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){
        saleChanceService.deleteSaleChance(ids);
        return success("success");
    }

}
