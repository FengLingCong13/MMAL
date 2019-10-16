package com.xhhp.mmall.task;

import com.xhhp.mmall.common.Const;
import com.xhhp.mmall.common.RedisShardedPool;
import com.xhhp.mmall.service.IOrderService;
import com.xhhp.mmall.util.RedisShardPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

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
    //@Scheduled(cron="0 */1 * * * ?")//每分钟执行一次gi
    public void closeOrderTaskV2() {
        log.info("关闭订单定时任务启动");
        long lockTimeout = 50000; //毫秒数
        Long setnxResult = RedisShardPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()));
        if(setnxResult != null && setnxResult.intValue() == 1) {
            //如果返回值是1，说明设置成功,获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            //没有获取到锁,继续判断时间戳，查看是否可以重置该锁
            String value = RedisShardPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            if(value != null && System.currentTimeMillis() > Long.parseLong(value)) {
                //重置分布式锁
                String getSetResult = RedisShardPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK,String.valueOf(System.currentTimeMillis()+lockTimeout));
                //双重检查
                if(getSetResult == null || (getSetResult!= null && StringUtils.equals(value, getSetResult))) {
                    //说明这个锁已经不存在了或者锁的value没有因为多进程被改变
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                } else {
                    log.info("没有获取到分布式锁");

                }
            } else {
                log.info("没有获取到分布式锁");
            }
        }
        log.info("关闭订单定时任务结束");
    }

    @Scheduled(cron="0 */1 * * * ?")//每分钟执行一次gi
    public void closeOrderTaskV3() {
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
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RedisShardPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放分布式锁");
    }

    @PreDestroy  //shutdown方法会执行的方法
    public void delLock() {
        RedisShardPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
    }
}
