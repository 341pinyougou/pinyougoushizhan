package cn.itcast.core.service;

import cn.itcast.common.utils.IdWorker;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.zookeeper.data.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private SeckillGoodsDao seckillGoodsDao;
    @Autowired
    private SeckillOrderDao seckillOrderDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private PayLogDao payLogDao;
    @Autowired
    private RedisTemplate redisTemplate;

    //添加商品订单到数据库
    @Override
    @Transactional
    public void submitOrder(Long seckillId,String name) {

        //通过秒杀商品id查询秒杀商品

        SeckillGoods seckillGoods = seckillGoodsDao.selectByPrimaryKey(seckillId);
        if (seckillGoods.getStockCount() <= 0){
            throw new RuntimeException("已经被抢完了,下次快点哦!!");
        }else {
            SeckillOrder seckillOrder = new SeckillOrder();
            //添加订单id
            seckillOrder.setId(idWorker.nextId());
            //添加秒杀商品id
            seckillOrder.setSeckillId(seckillId);
            //添加价格
            seckillOrder.setMoney(seckillGoods.getCostPrice());
            //添加用户id
            seckillOrder.setUserId(name);
            //添加商户id
            seckillOrder.setSellerId(seckillGoods.getSellerId());
            //订单创建时间
            seckillOrder.setCreateTime(new Date());
            //订单状态
            seckillOrder.setStatus("0");

            seckillOrderDao.insertSelective(seckillOrder);



            //保存日志表  (支付表)
            PayLog payLog = new PayLog();
            //ID
            payLog.setOutTradeNo(String.valueOf(idWorker.nextId()));

            //生成时间
            payLog.setCreateTime(new Date());
            //总金额 上面所有订单的金额
            payLog.setTotalFee(seckillOrder.getMoney().longValue()*100);

            //用户Id
            payLog.setUserId(seckillOrder.getUserId());

            //交易状态
            payLog.setTradeState("0");
            //支付类型
            payLog.setPayType("1");
            //订单集合   "1,2,3,4,5"
            payLog.setOrderList(seckillOrder.getId().toString().replace("[","").replace("]",""));

            //保存
            payLogDao.insertSelective(payLog);

            //保存缓存一份
            redisTemplate.boundHashOps("payLog").put(name,payLog);

            //更改数据库中(缓存库)中秒杀商品表的个数量
            updateSeckillGoodsNum(seckillId);

        }

    }

    public void updateSeckillGoodsNum(Long seckillId){
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setId(seckillId);
        Integer stockCount = seckillGoodsDao.selectByPrimaryKey(seckillId).getStockCount();
        seckillGoods.setStockCount(stockCount-1);
        seckillGoodsDao.updateByPrimaryKeySelective(seckillGoods);
    }
}
