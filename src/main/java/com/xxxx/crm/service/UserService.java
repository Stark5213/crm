package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserService extends BaseService<User,Integer> {

    @Resource
    private UserMapper userMapper;


    public UserModel userLogin(String userName,String userPwd){

        //1.验证参数
        checkParams(userName,userPwd);
        //2.通过用户名查询用户对象,返回user对象
        User user=userMapper.queryUserByName(userName);
        //3.判断用户对象是否为空
        AssertUtil.isTrue(null==user,"用户名不存在!");
        //4.校验密码
        checkPwd(userPwd,user.getUserPwd());
        //5.构建返回的用户模型
        return buildUserModel(user);

    }

    private UserModel buildUserModel(User user){
        UserModel userModel=new UserModel();
        //将用户ID加密存放
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }


    /**
     * 验证前后台传递的密码是否与数据库中的密码一致
     * @param userPwd
     * @param pwd
     */
    private void checkPwd(String userPwd, String pwd) {
        //将前台传递的密码是否与数据库中的密码一致
        userPwd= Md5Util.encode(userPwd);
        //比较密码是否一致
        AssertUtil.isTrue(!(userPwd.equals(pwd)),"用户密码不正确!");
    }

    /**
     * 验证用户登录参数
     * @param userName
     * @param userPwd
     */
    private void checkParams(String userName, String userPwd) {
        //用户名是否为空
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空!");
        //用户密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空!");
    }

    /**
     * 修改用户密码
     *     1. 参数校验
     *          1. 用户ID
     *              非空（判断是否登录状态）   用户对象存在
     *          2. 旧密码
     *              非空  旧密码与数据库中的密码是否一致
     *          3. 新密码
     *              非空  新密码与旧密码不能相同
     *          4. 确认密码
     *              非空  确认密码与新密码一致
     *     2. 设置新密码
     *          设置加密后的新密码
     *     3. 执行更新操作
     *          如果受影响的行数小于1，则表示失败
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassword(Integer userId, String oldPassword, String newPassword, String confirmPassword){

        // 1. 参数校验
        checkPassordParams(userId, oldPassword, newPassword, confirmPassword);

        // 2. 设置新密码
        User user = new User();
        user.setId(userId); // 设置主键
        user.setUserPwd(Md5Util.encode(newPassword)); // 设置新密码（加密）
        user.setUpdateDate(new Date()); // 设置修改时间

        // 3. 执行更新操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "用户密码修改失败！");

    }


    /**
     * 修改密码参数校验
     *    1. 用户ID
     *         非空（判断是否登录状态）   用户对象存在
     *    2. 旧密码
     *         非空  旧密码与数据库中的密码是否一致
     *    3. 新密码
     *         非空  新密码与旧密码不能相同
     *    4. 确认密码
     *         非空  确认密码与新密码一致
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */

    private void checkPassordParams(Integer userId, String oldPassword, String newPassword, String confirmPassword) {
        /* 用户ID */
        // 非空（判断是否登录状态）
        AssertUtil.isTrue(userId == null, "用户未登录！");
        // 通过用户ID查询用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        // 用户对象存在
        AssertUtil.isTrue( user == null, "用户不存在！");

        /* 旧密码（原始密码） */
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword), "原始密码不能为空！");
        AssertUtil.isTrue(!(Md5Util.encode(oldPassword).equals(user.getUserPwd())), "原始密码不正确！");

        /* 新密码 */
        AssertUtil.isTrue(StringUtils.isBlank(newPassword), "新密码不能为空！");
        AssertUtil.isTrue(newPassword.equals(oldPassword), "新密码与原始密码不能相同！");

        /* 确认密码 */
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword), "确认密码不能为空！");
        AssertUtil.isTrue(!confirmPassword.equals(newPassword), "确认密码与新密码不一致！");

    }

}
