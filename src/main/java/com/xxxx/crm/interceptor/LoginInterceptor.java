package com.xxxx.crm.interceptor;

import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 非法请求拦截
 *      判断用户是否是登录状态
 *          从cookie中获取用户Id，通过id查询用户对象
 *              如果用户ID不为空，且数据库中存在用户记录，则表示请求合法，放行资源
 *              如果用户ID为空，或数据库中查询不到记录，则请求不合法，抛出对应的异常
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserMapper userMapper;

    /**
     * 在目标方法执行前 拦截
     *      返回true，表示方法被执行
     *      返回false，表示方法不执行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 从cookie中获取用户ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 判断用户ID是否为空，且数据库中是否存在记录
        if (userId == null || userMapper.selectByPrimaryKey(userId) == null) {
            // 抛出异常
            throw new NoLoginException();
        }
        // 放行资源，执行目标资源
        return true;
    }
}
