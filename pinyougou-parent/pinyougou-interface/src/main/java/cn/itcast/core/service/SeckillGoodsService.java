package cn.itcast.core.service;

import cn.itcast.core.pojo.seckill.SeckillGoods;

import java.util.List;

public interface SeckillGoodsService {
    //秒杀首页查询回显
    List<SeckillGoods> findList();
    //秒杀商品详情页(直接查询数据库 没用redis)
    SeckillGoods findOneFromRedis(Long id);
}
