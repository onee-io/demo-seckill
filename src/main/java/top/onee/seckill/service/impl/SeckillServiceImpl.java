package top.onee.seckill.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import top.onee.seckill.dao.SeckillDao;
import top.onee.seckill.dao.SuccessKilledDao;
import top.onee.seckill.dto.Exposer;
import top.onee.seckill.dto.SeckillExecution;
import top.onee.seckill.entity.Seckill;
import top.onee.seckill.entity.Successkilled;
import top.onee.seckill.enums.SeckillStatEnum;
import top.onee.seckill.exception.RepeatKillException;
import top.onee.seckill.exception.SeckillCloseException;
import top.onee.seckill.exception.SeckillException;
import top.onee.seckill.service.SeckillService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 秒杀业务接口实现
 * Created by VOREVER on 08/04/2017.
 */
public class SeckillServiceImpl implements SeckillService {

    // 记录日志
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 加密盐值
    private final String salt = "Be6^$Kdb&uw74waf6#$Wr24f";

    @Resource
    private SeckillDao seckillDao;

    @Resource
    private SuccessKilledDao successKilledDao;

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
        Seckill seckill = seckillDao.queryById(seckillId);
        // 校验是否由此秒杀商品
        if (seckill == null) {
            return new Exposer(false, seckillId);
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

    @Override
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            // 数据篡改
            throw new SeckillException("seckill data rewrite");
        }
        Date now = new Date();
        try {
            // 减库存
            int updateCount = seckillDao.reduceNumber(seckillId, now);
            if (updateCount <= 0) {
                // 秒杀关闭（库存不足或时间不符）
                throw new SeckillCloseException("seckill close");
            } else {
                // 插入购买记录
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    // 重复秒杀
                    throw new RepeatKillException("repeate seckill");
                } else {
                    // 查询购买记录
                    Successkilled successkilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successkilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error: " + e.getMessage());
        }

    }
}
