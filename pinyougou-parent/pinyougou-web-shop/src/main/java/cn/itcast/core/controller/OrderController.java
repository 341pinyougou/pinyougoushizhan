package cn.itcast.core.controller;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.PageResult;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.OrderAndOrderItemVo;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody Order order){
        //商家ID
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        order.setSellerId(name);

        return orderService.search(page,rows,order);
    }
    //订单发货页面显示
    @RequestMapping("/findOrderAndOrderItem")
    public List<OrderAndOrderItemVo> findOrderAndOrderItem(){
        //商家ID
        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        return orderService.findOrderAndOrderItem(name);
    }
    //订单发货
    @RequestMapping("/fahuo")
    public Result fahuo(Long orderId){

        try {
            orderService.fahuo(orderId);
            return new Result(true,"发货成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"发货失败");
        }
    }
}
