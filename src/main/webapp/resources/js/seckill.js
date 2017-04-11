/**
 * Created by VOREVER on 11/04/2017.
 */
// javascript 模块化写法
var seckill = {
    // 封装秒杀相关ajax的url
    URL: {

    },
    // 验证手机号
    validatePhone: function (phone) {
        // 正则匹配手机号
        var regex = /^1(3|4|5|7|8)\d{9}$/;
        if (regex.test(phone)) {
            return true;
        } else {
            return false;
        }
    },
    // 详情页秒杀逻辑
    detail: {
        // 详情页初始化
        init: function (params) {
            // 在cookie中查找手机号
            var userPhone = $.cookie('userPhone');
            // 验证手机号
            if (!seckill.validatePhone(userPhone)) {
                // 绑定手机号 弹出框
                $('#killPhoneModal').modal({
                    show: true,             // 显示弹出层
                    backdrop: 'static',     // 禁用位置关闭
                    keyboard: false         // 关闭键盘事件
                });
                $('#userPhoneBtn').click(function (e) {
                    e.stopPropagation();
                    var userPhone = $('#userPhoneKey').val();
                    if (seckill.validatePhone(userPhone)) {
                        // 电话写入cookie
                        $.cookie('userPhone', userPhone, {
                            expires: 7,         // 有效期7天
                            path: '/seckill'    // 在此路径下有效
                        });
                        // 验证通过 刷新页面
                        window.location.reload();
                    } else {
                        // 先隐藏 加载完内容后再展示 避免页面出现中间过程
                        $('#userPhoneMessage').hide().html('<label class="label label-danger">手机号错误！</label>').show(300);
                    }
                });
            }
            // 用户已登陆
        }
    }

}