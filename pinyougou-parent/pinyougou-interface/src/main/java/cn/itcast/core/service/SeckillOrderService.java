package cn.itcast.core.service;

public interface SeckillOrderService {
    //提交秒杀订单
    void submitOrder(Long seckillId,String name);
}
