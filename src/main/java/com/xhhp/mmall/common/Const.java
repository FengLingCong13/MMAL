package com.xhhp.mmall.common;

/**
 * Const class
 *
 * @author Flc
 * @date 2019/9/26
 */
public class Const {

    public static final String CurrentUser = "currentUser";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public interface RedisCacheExtime {
        int REDIS_SESSION_EXTIME = 30*60;   //30分钟
    }

    public interface Role{
        int ROLE_CUSTOMER=0;//普通用户
        int ROLE_ADMIN=1;//管理员
    }

    public interface Cart {
        int CHECKED = 1; //购物车选中状态
        int UN_CHECKED = 0; //购物车未选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";   //购物车数量大于库存
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS"; //购物车数量小于库存
    }

    public enum OrderStatusEnum {
        CANCELED(0,"取消"),
        NO_PAY(10,"未支付"),
        PAID(20,"已付款"),
        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单完成"),
        ORDER_CLOSE(60,"订单关闭");

        private Integer code;
        private String value;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        OrderStatusEnum(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        public static OrderStatusEnum codeOf(int code) {
            for(OrderStatusEnum orderStatusEnum :values()) {
                if(orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw  new RuntimeException("没有找到对应的枚举");
        }
    }

    public enum PaymentTypeEnum {
        OLINE_PAY("在线支付",1);
        private String value;
        private Integer code;

        PaymentTypeEnum(String value, Integer code) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public static PaymentTypeEnum codeOf(int code) {
            for(PaymentTypeEnum paymentTypeEnum :values()) {
                if(paymentTypeEnum.getCode() == code) {
                    return paymentTypeEnum;
                }
            }
            throw  new RuntimeException("没有找到对应的枚举");
        }
    }

    public interface REDIS_LOCK {
        String CLOSE_ORDER_TASK_LOCK = "CLOSE_ORDER_TASK_LOCK"; //关闭订单的分布式锁
    }
}
