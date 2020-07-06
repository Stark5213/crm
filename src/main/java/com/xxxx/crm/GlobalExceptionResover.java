package com.xxxx.crm;

import com.alibaba.fastjson.JSON;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * 全局异常统一处理
 *      控制器（Controller）中方法的返回值有两种情况：
 *          1. 视图
 *          2. JSON数据
 *      方法返回值：
 *          1. 视图  2. JSON数据
 *          如果方法上没有声明@ResponseBody注解，则表示返回的是视图；
 *          反之，如果声明了，则表示返回的是JSON格式
 */
@Component
public class GlobalExceptionResover implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse, Object handler, Exception ex) {


        /* 判断用户是否是登录状态，进行对应的拦截 */
        if (ex instanceof NoLoginException) {
            // 拦截跳转到登录页面
            ModelAndView mv = new ModelAndView("redirect:/index");
            return mv;
        }

        // 设置默认的视图处理异常
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.addObject("code",300);
        modelAndView.addObject("msg", "系统异常，请稍后再试...");

        // 判断handler是否是HandlerMethod的实例
        if (handler instanceof HandlerMethod) {
            // 类型转换
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 得到方法上的@ResponseBody
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            // 判断注解是否存在
            if (responseBody == null) {
                /**
                 * 方法返回的是视图
                 */
                // 判断异常是否是自定义异常，如果是，则返回自定义异常设置的code和msg
                if (ex instanceof ParamsException) {
                    ParamsException p = (ParamsException) ex;
                    modelAndView.addObject("code", p.getCode());
                    modelAndView.addObject("msg", p.getMsg());
                }
                return modelAndView;

            } else {
                /**
                 * 方法返回的是JSON数据
                 */
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统异常，请重试...");

                // 如果是自定义异常
                if (ex instanceof ParamsException){
                    ParamsException p = (ParamsException) ex;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }

                // 设置响应的MIME类型和编码格式
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                try {
                    // 得到字符输出流
                    PrintWriter writer = httpServletResponse.getWriter();
                    // 将resultInfo对象转换成JSON格式的字符串
                    String json = JSON.toJSONString(resultInfo);
                    // 输出数据
                    writer.write(json);
                    // 刷新并关闭
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        return modelAndView;
    }
}
