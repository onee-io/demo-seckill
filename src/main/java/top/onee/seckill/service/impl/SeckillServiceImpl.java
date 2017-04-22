package top.onee.seckill.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import top.onee.seckill.dao.SeckillDao;
import top.onee.seckill.dao.SuccessKilledDao;
import top.onee.seckill.dao.cache.RedisDao;
import top.onee.seckill.dto.Exposer;
import top.onee.seckill.dto.SeckillExecution;
import top.onee.seckill.entity.Seckill;
import top.onee.seckill.entity.Successkilled;
import top.onee.seckill.enums.SeckillStatEnum;
import top.onee.seckill.exception.RepeatKillException;
import top.onee.seckill.exception.SeckillCloseException;
import top.onee.seckill.exception.SeckillException;
import top.onee.seckill.service.SeckillService;

import java.util.Date;
import java.util.List;

/**
 * 秒杀业务接口实现
 * Created by VOREVER on 08/04/2017.
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    // 记录日志
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 加密盐值
    private final String salt = "Be6^$Kdb&uw74waf6#$Wr24f";

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getSeckillById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        // 访问redis检索是否有此商品
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            // 访问数据库检索 校验是否由此秒杀商品
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                // 存入redis
                redisDao.putSeckill(seckill);
            }
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        // 校验秒杀时间
        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        // 正常秒杀
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }


    /**
     * 使用Spring注解控制事务的优点：
     * 1、开发团队达成一致的约定，明确标注事务方法的编程风格
     * 2、保证事务方法的执行时间尽可能断，不要穿插其他的网络操作，
     * 3、不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
     * 注：只有捕获运行期异常（RuntimeException）才会进行回滚操作
     */
    @Override
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillCloseException, RepeatKillException, SeckillException{
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            // 数据篡改
            throw new SeckillException("seckill data rewrite");
        }
        Date now = new Date();
        try {
            // 插入购买记录
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                // 重复秒杀
                throw new RepeatKillException("repeate seckill");
            } else {
                // 减库存
                int updateCount = seckillDao.reduceNumber(seckillId, now);
                if (updateCount <= 0) {
                    // 秒杀关闭（库存不足或时间不符）
                    throw new SeckillCloseException("seckill close");
                } else {
                    // 查询购买记录
                    Successkilled successkilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successkilled);
                }
            }
        } catch (SeckillCloseException e) {
            throw e;
        } catch (RepeatKillException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error: " + e.getMessage());
        }
    }
}
