package top.onee.seckill.enums;

/**
 * 秒杀结果状态枚举
 * Created by VOREVER on 08/04/2017.
 */
public enum SeckillStatEnum {
    SUCCESS(1, "秒杀成功"),
    END(1, "秒杀结束"),
    REPEAT_KILL(1, "重复秒杀"),
    INNER_ERROR(1, "系统异常"),
    DATA_REWRITE(1, "数据篡改");

    private int state;

    private String info;

    SeckillStatEnum(int state, String info) {
        this.state = state;
        this.info = info;
    }

    public int getState() {
        return state;
    }

    public String getInfo() {
        return info;
    }

    public static SeckillStatEnum stateOf(int index) {
        for (SeckillStatEnum statEnum: values()) {
            if (statEnum.getState() == index) {
                return statEnum;
            }
        }
        return null;
    }
}
