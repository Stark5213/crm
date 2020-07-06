layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    /**
     * 用户修改密码 表单提交
     */
    form.on('submit(saveBtn)', function (data) {
        // 得到表单的全部字段
        var fieldData = data.field;

        // TODO 参数校验    1. 非空校验 2. 是否相等

        // 发送ajax请求到后台的修改方法
        $.ajax({
            type:"post",
            // http://localhost:8080/crm/user/updatePassword
            url: ctx + "/user/updatePassword",
            data:{
                oldPassword:fieldData.old_password,
                newPassword:fieldData.new_password,
                confirmPassword:fieldData.again_password
            },
            success:function (resultInfo) {
                // 判断是否更新成功
                if (resultInfo.code == 200) {

                    // 修改成功后，跳转到登录页面，清除cookie
                    layer.msg("用户密码修改成功，系统将在3秒钟后退出...",function () {
                        // 退出系统后，删除对应的cookie
                        $.removeCookie("userIdStr", {domain:"localhost",path:"/crm"});
                        $.removeCookie("userName", {domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName", {domain:"localhost",path:"/crm"});

                        // 跳转到登录页面 (父窗口跳转)
                        window.parent.location.href = ctx + "/index";

                    });

                } else {
                    layer.msg(resultInfo.msg, {icon: 5});
                }

            }
        });

    });


});