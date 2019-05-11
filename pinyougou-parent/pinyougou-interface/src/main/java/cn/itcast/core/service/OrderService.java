package cn.itcast.core.service;

import cn.itcast.core.pojo.order.Order;
import entity.PageResult;
import vo.MyOrder;
import vo.OrderAndOrderItemVo;
import vo.SellerOrderVo;

import java.util.List;
import java.util.Map;

public interface OrderService {
    void add(Order order);

    PageResult search(Integer page, Integer rows, Order order);

    List<OrderAndOrderItemVo> findOrderAndOrderItem(String name);

    List<SellerOrderVo> findSellerOrder();

    List<MyOrder> findOrderitemList(String name);

    List<MyOrder> findAllOrder(String name);
}
