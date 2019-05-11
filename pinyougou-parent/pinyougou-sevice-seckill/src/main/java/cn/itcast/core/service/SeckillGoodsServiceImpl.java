package cn.itcast.core.service;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {


    @Autowired
    private SeckillGoodsDao seckillGoodsDao;


    //秒杀首页查询回显
    @Override
    public List<SeckillGoods> findList() {
        SeckillGoodsQuery seckillGoodsQuery = new SeckillGoodsQuery();
        SeckillGoodsQuery.Criteria criteria = seckillGoodsQuery.createCriteria();
        //审核通过的商品
        criteria.andStatusEqualTo("1");
        //库存大于0的
        criteria.andStockCountGreaterThan(0);
        //开始时间小于当前时间
        criteria.andStartTimeLessThanOrEqualTo(new Date());
        //结束时间大于当前时间
        criteria.andEndTimeGreaterThan(new Date());

        return seckillGoodsDao.selectByExample(seckillGoodsQuery);
    }

    //秒杀商品详情页(没用redis,直接查询数据库)
    @Override
    public SeckillGoods findOneFromRedis(Long id) {
        return seckillGoodsDao.selectByPrimaryKey(id);
    }
}
