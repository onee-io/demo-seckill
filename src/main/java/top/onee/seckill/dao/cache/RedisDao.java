package top.onee.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import top.onee.seckill.entity.Seckill;

/**
 * Created by VOREVER on 15/04/2017.
 */
public class RedisDao {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    public RedisDao(String host, int port) {
        jedisPool = new JedisPool(host, port);
    }

    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public Seckill getSeckill(long seckillId) {
        // redis操作
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckillId;
                // 采用内部序列化操作
                // protostuff：POJO
                byte[] bytes = jedis.get(key.getBytes());
                // 如果获取到缓存
                if (bytes != null) {
                    // 空对象用于存放反序列化后的对象
                    Seckill seckill = schema.newMessage();
                    // 反序列化对象
                    ProtobufIOUtil.mergeFrom(bytes, seckill, schema);
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public String putSeckill(Seckill seckill) {
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                // 序列化对象
                byte[] bytes = ProtobufIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                // 存储到redis，成功返回"OK"，失败则返回错误信息
                // 超时缓存
                int timeout = 60 * 60; // 1小时
                String result = jedis.setex(key.getBytes(), timeout, bytes);
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
