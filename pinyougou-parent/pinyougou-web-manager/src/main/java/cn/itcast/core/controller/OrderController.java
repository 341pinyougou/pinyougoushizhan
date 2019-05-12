package cn.itcast.core.controller;


import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.OrderAndOrderItemVo;
import vo.SellerOrderVo;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @RequestMapping("/findXS")
    public List<SellerOrderVo> findXS(){
        List<SellerOrderVo> list = orderService.findSellerOrder();
        return list;
    }
}
