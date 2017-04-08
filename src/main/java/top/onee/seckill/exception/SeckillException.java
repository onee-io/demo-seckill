package top.onee.seckill.exception;

/**
 * 秒杀相关的所有业务异常
 * mysql只支持运行期异常的回滚操作
 * Created by VOREVER on 08/04/2017.
 */
public class SeckillException extends RuntimeException {

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
