package cn.itcast.core.controller;

import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.SeckillService;
import com.alibaba.dubbo.config.annotation.Reference;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import vo.SeckillGoodsVO;

import java.util.List;

@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Reference
    private SeckillService seckillService;


    //根据商品id查询商品,库存
    @RequestMapping("/findOne")
    public SeckillGoodsVO findOne(Long goodsId){
        System.out.println(goodsId);
        return seckillService.findOne(goodsId);
    }

    //根据库存id查询当前款价格,库存
    @RequestMapping("/findByitemId")
    public Item findByitemId(Long itemId){
        return seckillService.findByitemId(itemId);
    }

    //保存秒杀数据,申请秒杀
    @RequestMapping("/save")
    public Result save(@RequestBody SeckillGoodsVO seckillGoodsVO){
        try {

            System.out.println(seckillGoodsVO);
            seckillService.save(seckillGoodsVO);
            return new Result(true,"申请成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"申请失败");
        }
    }

    //回显秒杀数据
    @RequestMapping("/findSeckillGoodsList")
    public List<SeckillGoods> findSeckillGoodsList(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return seckillService.findSeckillGoodsList(name);
    }
}
