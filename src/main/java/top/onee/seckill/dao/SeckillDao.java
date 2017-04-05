package top.onee.seckill.dao;

import org.apache.ibatis.annotations.Param;
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
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

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
     * java不会保留形参，传到xml中会变为arg0、arg1，致使报错找不到offset
     * 使用MyBatis提供的注解传递形参
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

}
