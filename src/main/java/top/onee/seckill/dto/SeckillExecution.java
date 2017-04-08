package top.onee.seckill.dto;

import top.onee.seckill.entity.Successkilled;
import top.onee.seckill.enums.SeckillStatEnum;

/**
 * 秒杀执行结果
 * Created by VOREVER on 08/04/2017.
 */
public class SeckillExecution {

    // 秒杀ID
    private long seckillId;

    // 秒杀执行结果的状态
    private int state;

    // 状态的标识
    private String stateInfo;

    // 秒杀成功时，返回秒杀成功的对象
    private Successkilled successkilled;

    // 秒杀成功返回所有信息
    public SeckillExecution(long seckillId, SeckillStatEnum seckillStatEnum, Successkilled successkilled) {
        this.seckillId = seckillId;
        this.state = seckillStatEnum.getState();
        this.stateInfo = seckillStatEnum.getInfo();
        this.successkilled = successkilled;
    }

    // 秒杀失败
    public SeckillExecution(long seckillId, SeckillStatEnum seckillStatEnum) {
        this.seckillId = seckillId;
        this.state = seckillStatEnum.getState();
        this.stateInfo = seckillStatEnum.getInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public Successkilled getSuccesskilled() {
        return successkilled;
    }

    public void setSuccesskilled(Successkilled successkilled) {
        this.successkilled = successkilled;
    }
}
