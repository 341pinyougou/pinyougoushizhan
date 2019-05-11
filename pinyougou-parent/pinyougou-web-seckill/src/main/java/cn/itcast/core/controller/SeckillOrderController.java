package cn.itcast.core.controller;


import cn.itcast.core.service.SeckillOrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckillOrder")
public class SeckillOrderController {


    @Reference
    private SeckillOrderService seckillOrderService;

    //秒杀商品订单提交
    @RequestMapping("/submitOrder")
    public Result submitOrder(Long seckillId){
        try {
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            seckillOrderService.submitOrder(seckillId,name);
            return new Result(true,"成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }
    }
}
