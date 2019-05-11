package cn.itcast.core.controller;

import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.SeckillGoodsService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {

    @Reference
    private SeckillGoodsService seckillGoodsService;

    //秒杀首页
    @RequestMapping("/findList")
    public List<SeckillGoods> findList(){
        return seckillGoodsService.findList();
    }

    //秒杀商品详情(没用redis,直接从库里取)
    @RequestMapping("/findOneFromRedis")
    public SeckillGoods findOneFromRedis(Long id){
        return seckillGoodsService.findOneFromRedis(id);
    }


}
