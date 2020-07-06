package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.service.CusDevPlanService;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.vo.CusDevPlan;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {

    @Resource
    private SaleChanceService saleChanceService;

    @Resource
    private CusDevPlanService cusDevPlanService;

    /**
     * 进入客户开发计划页面
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "cusDevPlan/cus_dev_plan";
    }

    /**
     * 加载计划项数据页面
     * @return
     */
    @RequestMapping("toCusDevPlanDataPage")
    public String toCusDevPlanDataPage(Integer sId, Model model) {

        // 通过ID查询营销机会数据
        if (sId != null) {
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(sId);
            // 设置请求域
            model.addAttribute("saleChance",saleChance);
        }

        return "cusDevPlan/cus_dev_plan_data";
    }

    /**
     * 通过营销机会ID查询计划项列表
     * @param query
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryCusDevPlan(CusDevPlanQuery query) {
        return cusDevPlanService.queryByParamsForTable(query);
    }


    /**
     * 进入添加或修改计划项页面
     * @return
     */
    @RequestMapping("addOrUpdateCusDevPlanPage")
    public String addOrUpdateCusDevPlanPage(Integer cId, Model model, Integer sid) {

        // 设置请求域
        model.addAttribute("sid",sid);

        if (cId != null) {
            // 通过id查询计划项数据
            CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(cId);
            // 设置请求域
            model.addAttribute("cusDevPlan", cusDevPlan);
        }

        return "cusDevPlan/add_update";
    }


    /**
     * 计划项添加
     * @param cusDevPlan
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("success");
    }

    /**
     * 计划项更新
     * @param cusDevPlan
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan){
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("success");
    }


}
