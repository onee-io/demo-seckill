package top.onee.seckill.service;

import top.onee.seckill.dto.Exposer;
import top.onee.seckill.dto.SeckillExecution;
import top.onee.seckill.entity.Seckill;
import top.onee.seckill.exception.RepeatKillException;
import top.onee.seckill.exception.SeckillCloseException;
import top.onee.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在使用者的角度来设计接口
 * 1、方法定义粒度：方法定义要非常清除
 * 2、参数：越简练越好
 * 3、返回类型：类型一定的要友好，或者return我们允许的异常
 *
 * Created by VOREVER on 08/04/2017.
 */
public interface SeckillService {

    /**
     * 查询全部秒杀记录
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getSeckillById(long seckillId);

    /**
     * 在秒杀开启时输出秒杀接口的地址，否则输出系统时间和秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作，可能成功，可能失败，允许抛出异常
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillCloseException, RepeatKillException, SeckillException;
}
