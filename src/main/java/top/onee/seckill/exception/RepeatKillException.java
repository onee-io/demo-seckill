package top.onee.seckill.exception;

/**
 * 重复秒杀异常
 * Created by VOREVER on 08/04/2017.
 */
public class RepeatKillException extends SeckillException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
