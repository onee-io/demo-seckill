package top.onee.seckill.exception;

/**
 * 秒杀关闭异常
 * Created by VOREVER on 08/04/2017.
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
