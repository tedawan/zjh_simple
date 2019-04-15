package com.addplus.server.api.modelenum;

/**
 * 类名: QueueEnum
 *
 * @author zhangjiehang
 * @version V1.0
 * @date 2018/9/27 下午12:06
 * @description 类描述: RabbitMQ队列初始化枚举类
 */
public enum QueueEnum {
    /**
     * 订单超时队列
     */
    ORDER_QUEUE("order_exchange", "order_queue", "order_routingkey"),
    /**
     * 短信队列
     */
    MSM_QUEUW("msm_exchange", "msm_queue", "msm_routingkey"),
    /**
     * 库存加减队列
     */
    STOCK_MANAGER("stock_manager_exchange", "stock_manager_queue", "stock_manager_routingkey"),
    /**
     * 拼团库存加减队列
     */
    FIGHT_STOCK_QUEUQ("fight_stock_manager_exchange", "fight_stock_manager_queue", "fight_stock_manager_routingkey");

    QueueEnum(String exchange, String queue, String routingKey) {
        this.exchange = exchange;
        this.queue = queue;
        this.routingKey = routingKey;
    }

    private String exchange;

    private String queue;

    private String routingKey;

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }
}
