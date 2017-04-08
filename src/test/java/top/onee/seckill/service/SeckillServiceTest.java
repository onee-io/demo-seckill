package top.onee.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.onee.seckill.dto.Exposer;
import top.onee.seckill.dto.SeckillExecution;
import top.onee.seckill.entity.Seckill;
import top.onee.seckill.exception.RepeatKillException;
import top.onee.seckill.exception.SeckillCloseException;

import java.util.List;

/**
 * Created by VOREVER on 08/04/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"
})
public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckillList = seckillService.getSeckillList();
        logger.info("seckillList = {}", seckillList);
    }

    @Test
    public void getSeckillById() throws Exception {
        long seckillId = 1000L;
        Seckill seckill = seckillService.getSeckillById(seckillId);
        logger.info("seckill = {}", seckill);
    }

    // 继承测试完整秒杀逻辑
    @Test
    public void testSeckillLogic() throws Exception {
        long seckillId = 1001L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            long userPhone = 18611875960L;
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, userPhone, exposer.getMd5());
                logger.info("seckillExecution = {}", seckillExecution);
            } catch (SeckillCloseException e1) {
                logger.error(e1.getMessage());
            } catch (RepeatKillException e2) {
                logger.error(e2.getMessage());
            }
        } else {
            logger.warn("秒杀未开启");
        }
    }

}