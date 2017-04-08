package top.onee.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.onee.seckill.entity.Successkilled;

import javax.annotation.Resource;

/**
 * Created by VOREVER on 05/04/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        long id = 1000L;
        long phone = 18611875958L;
        int insertCount = successKilledDao.insertSuccessKilled(id, phone);
        System.out.println("insertCount = " + insertCount);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        long id = 1000L;
        long phone = 18611875958L;
        Successkilled successkilled = successKilledDao.queryByIdWithSeckill(id, phone);
        System.out.println(successkilled);
        System.out.println(successkilled.getSeckill());
    }

}