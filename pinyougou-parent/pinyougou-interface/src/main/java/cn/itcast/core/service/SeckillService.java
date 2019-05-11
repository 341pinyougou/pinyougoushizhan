package cn.itcast.core.service;

import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import vo.SeckillGoodsVO;

import java.util.List;

public interface SeckillService {
    //回显当前商品
    SeckillGoodsVO findOne(Long goodsId);
    //根据库存id查询当前价格,库存数
    Item findByitemId(Long itemId);
    //申请秒杀
    void save(SeckillGoodsVO seckillGoodsVO);
    //回显秒杀数据
    List<SeckillGoods> findSeckillGoodsList(String name);
    //回显全部秒杀数据
    List<SeckillGoods> findAllSeckillGoodsList();
    //开始审核
    void updateStatus(Long[] ids, String status);
}
