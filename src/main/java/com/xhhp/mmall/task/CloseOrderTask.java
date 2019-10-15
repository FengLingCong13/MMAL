package com.xhhp.mmall.task;

import com.xhhp.mmall.common.Const;
import com.xhhp.mmall.common.RedisShardedPool;
import com.xhhp.mmall.service.IOrderService;
import com.xhhp.mmall.util.RedisShardPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * CloseOrderTask class
 *
 * @author Flc
 * @date 2019/10/15
 */
@Component
@Slf4j
public class CloseOrderTask {

    private IOrderService iOrderService;

    //@Scheduled(cron="0 */1 * * * ?")//每分钟执行一次
    public void closeOrderTaskV1() {
        log.info("关闭订单定时任务启动");
        int hour = 2;
        //iOrderService.closeOrder(hour);
        log.info("关闭订单定时任务结束");
    }
    @Scheduled(cron="0 */1 * * * ?")//每分钟执行一次gi
    public void closeOrderTaskV2() {
        log.info("关闭订单定时任务启动");
        long lockTimeout = 50000; //毫秒数
        Long setnxResult = RedisShardPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()));
        if(setnxResult != null && setnxResult.intValue() == 1) {
            //如果返回值是1，说明设置成功,获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            log.info("没有获得分布式锁");
        }
        log.info("关闭订单定时任务结束");
    }

    private void closeOrder(String lockName) {
        log.info("获取分布式锁");
        RedisShardPoolUtil.expire(lockName,50);//有效期50秒，防止死锁
        //iOrderService.closeOrder(2);
        RedisShardPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放分布式锁");
    }
}
