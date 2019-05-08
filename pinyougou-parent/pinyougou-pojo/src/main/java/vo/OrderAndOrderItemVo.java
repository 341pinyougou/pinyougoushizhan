package vo;

import cn.itcast.core.pojo.order.OrderItem;

import java.io.Serializable;
import java.util.List;

public class OrderAndOrderItemVo implements Serializable {

    private Long orderId;
    private List<OrderItem> orderItemList;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    @Override
    public String toString() {
        return "OrderAndOrderItemVo{" +
                "orderId=" + orderId +
                ", orderItemList=" + orderItemList +
                '}';
    }
}
