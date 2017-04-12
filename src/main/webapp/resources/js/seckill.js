/**
 * Created by VOREVER on 11/04/2017.
 */
// javascript 模块化写法
var seckill = {

    // 封装秒杀相关ajax的url
    URL: {
        now: function() {
            return '/seckill/time/now';
        },
        exposer: function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution'
        }
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

    // 倒计时逻辑
    countDown: function (seckillId, now, startTime, endTime) {
        // 获取展示信息的节点
        var seckillBox = $('#seckill-box');
        if (startTime > endTime) {
            // 系统时间错误
            seckillBox.html('系统时间错误');
        } else if (now > endTime) {
            // 秒杀结束
            seckillBox.html('秒杀结束');
        } else if (now < startTime) {
            // 秒杀未开始
            var killTime = new Date(startTime + 1000);  //防止加载页面时 时间偏移
            seckillBox.countdown(killTime, function (event) {
                // 时间格式
                var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown', function () {
                // 绑定倒计时结束时监听时间
                seckill.handlerSeckill(seckillId, seckillBox);
            })
        } else {
            // 秒杀开始
            seckill.handlerSeckill(seckillId, seckillBox);
        }
    },

    // 执行秒杀逻辑
    handlerSeckill: function (seckillId, node) {
        // 获取秒杀地址 控制显示器 执行秒杀
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        // 获取秒杀地址
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
            if (result && result['success']) {
                var exposer = result['data'];
                if (exposer['exposed']) {
                    // 秒杀已开启
                    // 获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    // 绑定一次监听事件
                    $('#killBtn').one('click', function () {
                        // 禁用按钮 防止用户多次点击
                        $(this).addClass('disable');
                        // 执行秒杀
                        $.post(killUrl, {}, function (result) {
                            if (result && result['success']) {
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                node.html('<span class="label label-success">' + stateInfo + '</span>')
                            } else {
                                console.log('result', result);
                            }
                        });
                    });
                    // 展示秒杀按钮
                    node.show();
                } else {
                    // 秒杀未开启
                    var now = exposer['now'];
                    var startTime = exposer['start'];
                    var endTime = exposer['end'];
                    // 重新倒计时
                    seckill.countDown(seckillId, now, startTime, endTime);
                }
            } else {
                console.log('result', result);
            }
        })
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
            var seckillId = params['seckillId'];
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    var now = result['data'];
                    // 执行计时逻辑
                    seckill.countDown(seckillId, now, startTime, endTime);
                } else {
                    console.log('result', result);
                }
            });
        }
    }

};