package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.ParamsException;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import org.apache.ibatis.javassist.bytecode.stackmap.BasicBlock;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @PostMapping("user/login")
    @ResponseBody
    public ResultInfo userLogin(String userName,String userPwd){
       /* ResultInfo resultInfo=new ResultInfo();
        //通过try catch 捕获 Service 层抛出的异常
        try{
            //调用service的方法,返回用户模型
            UserModel userModel=userService.userLogin(userName,userPwd);
            *//**
             * 登录成功后，有两种处理：
             *  1. 将用户的登录信息存入 Session （ 问题：重启服务器，Session 失效，客户端需要重复登录 ）
             *  2. 将用户信息返回给客户端，由客户端（Cookie）保存
             *//*
            //将用户模型数据设置到resultInfo
            resultInfo.setResult(userModel);

        }catch(ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        } catch(Exception e){
            resultInfo.setCode(300);
            resultInfo.setMsg("登录失败!");
            e.printStackTrace();
        }

        return resultInfo;*/
        ResultInfo resultInfo=new ResultInfo();
        UserModel userModel=userService.userLogin(userName,userPwd);
        resultInfo.setResult(userModel);
        return resultInfo;

    }
    /**
     * 修改用户密码
     * @return
     */
    @PostMapping("user/updatePassword")
    @ResponseBody
    public ResultInfo updatePassword(HttpServletRequest request,
                                     String oldPassword, String newPassword, String confirmPassword ){
        /*ResultInfo resultInfo = new ResultInfo();
        try{
            // 从cookie中获取用户ID
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            // 调用service的方法
            userService.updatePassword(userId,oldPassword,newPassword, confirmPassword);
        } catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        } catch (Exception e){
            resultInfo.setCode(300);
            resultInfo.setMsg("操作失败！");
        }
        return resultInfo;*/

        ResultInfo resultInfo=new ResultInfo();
        //从cookie中获取用户ID
        Integer userId=LoginUserUtil.releaseUserIdFromCookie(request);
        //调用service的方法
        userService.updatePassword(userId,oldPassword,newPassword,confirmPassword);
        return  resultInfo;

    }

    @RequestMapping("user/toPasswordPage")
    public String toPasswordPage(){
        return "user/password";

    }

}
