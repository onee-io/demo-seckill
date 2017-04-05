package top.onee.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.onee.seckill.entity.Seckill;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by VOREVER on 05/04/2017.
 */
// 配置spring和junit的整合，junit启动时加载springIOC容器
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    @Resource
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() throws Exception {
        Date date = new Date();
        int updateCount = seckillDao.reduceNumber(1000L, date);
        System.out.println("updateCount = " + updateCount);
    }

    @Test
    public void queryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void queryAll() throws Exception {
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        for (Seckill seckill: seckills) {
            System.out.println(seckill);
        }
    }

}