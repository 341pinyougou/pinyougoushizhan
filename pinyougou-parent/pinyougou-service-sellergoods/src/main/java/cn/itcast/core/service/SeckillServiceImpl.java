package cn.itcast.core.service;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import vo.SeckillGoodsVO;

import java.util.Date;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private SeckillGoodsDao seckillGoodsDao;
    @Autowired
    private RedisTemplate redisTemplate;


    //回显当前商品
    @Override
    public SeckillGoodsVO findOne(Long goodsId) {
        SeckillGoodsVO seckillGoodsVO = new SeckillGoodsVO();
        //通过商品id查询商品
        Goods goods = goodsDao.selectByPrimaryKey(goodsId);
        //商品
        seckillGoodsVO.setGoods(goods);
        //通过商品id查询库存表
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(goodsId);
        List<Item> itemList = itemDao.selectByExample(itemQuery);
        //库存
        seckillGoodsVO.setItemList(itemList);


        return seckillGoodsVO;
    }

    //根据库存id查询当前价格,库存数
    @Override
    public Item findByitemId(Long itemId) {
        return itemDao.selectByPrimaryKey(itemId);
    }

    //申请秒杀
    @Override
    public void save(SeckillGoodsVO seckillGoodsVO) {
        SeckillGoods seckillGoods = seckillGoodsVO.getSeckillGoods();
        //商品id
        seckillGoods.setGoodsId(seckillGoodsVO.getGoods().getId());
        //库存id
        seckillGoods.setItemId(seckillGoodsVO.getItem().getId());
        //图片

        seckillGoods.setSmallPic(seckillGoodsVO.getItem().getImage());
        //原价格
        seckillGoods.setPrice(seckillGoodsVO.getItem().getPrice());
        //商家Id
        seckillGoods.setSellerId(seckillGoodsVO.getGoods().getSellerId());
        //添加日期
        seckillGoods.setCreateTime(new Date());
        //审核状态
        seckillGoods.setStatus("0");
        //剩余库存数量
        seckillGoods.setNum(seckillGoodsVO.getItem().getNum());
        seckillGoods.setNum(seckillGoodsVO.getSeckillGoods().getStockCount());
        //描述
        seckillGoods.setIntroduction(seckillGoodsVO.getGoods().getCaption());

        seckillGoodsDao.insertSelective(seckillGoods);
    }

    //回显秒杀数据
    @Override
    public List<SeckillGoods> findSeckillGoodsList(String name) {
        SeckillGoodsQuery seckillGoodsQuery = new SeckillGoodsQuery();
        seckillGoodsQuery.createCriteria().andSellerIdEqualTo(name);
        return seckillGoodsDao.selectByExample(seckillGoodsQuery);
    }

    //回显全部秒杀数据
    @Override
    public List<SeckillGoods> findAllSeckillGoodsList() {
        return seckillGoodsDao.selectByExample(null);
    }

    //开始审核
    @Override
    public void updateStatus(Long[] ids, String status) {
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setStatus(status);
        for (Long id : ids) {
            seckillGoods.setId(id);
            seckillGoodsDao.updateByPrimaryKeySelective(seckillGoods);
            //TODO 添加索引库

            //TODO 生成静态化页面

        }
    }
}
