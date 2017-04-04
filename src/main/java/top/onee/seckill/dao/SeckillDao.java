package top.onee.seckill.dao;

import top.onee.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * Created by VOREVER on 04/04/2017.
 */
public interface SeckillDao {

    /**
     * 减库存
     * @param seckillId
     * @param killTime
     * @return 返回更新的行数
     */
    int reduceNumber(long seckillId, Date killTime);

    /**
     * 根据ID查询秒杀对象
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(int offset, int limit);

}
