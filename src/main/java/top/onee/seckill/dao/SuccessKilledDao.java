package top.onee.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.onee.seckill.entity.Successkilled;

/**
 * Created by VOREVER on 04/04/2017.
 */
@Repository
public interface SuccessKilledDao {

    /**
     * 插入购买明细，可通过联合主键去重
     * @param suckillId
     * @param userPhone
     * @return 插入的行数
     */
    int insertSuccessKilled(@Param("suckillId") long suckillId, @Param("userPhone") long userPhone);

    /**
     * 根据ID查询SuccessKilled并携带秒杀产品对象实体
     * @param seckilled
     * @return
     */
    Successkilled queryByIdWithSeckill(@Param("seckilled") long seckilled, @Param("userPhone") long userPhone);

}
