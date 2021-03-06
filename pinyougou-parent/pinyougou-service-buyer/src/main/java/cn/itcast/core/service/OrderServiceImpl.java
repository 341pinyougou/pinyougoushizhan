package cn.itcast.core.service;

import cn.itcast.common.utils.IdWorker;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import vo.Cart;
import vo.MyOrder;
import vo.OrderAndOrderItemVo;
import vo.SellerOrderVo;

import java.math.BigDecimal;
import java.util.*;

/**
 * 订单管理
 */
@Service
@Transactional
public class OrderServiceImpl implements  OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private PayLogDao payLogDao;

    //保存订单
    @Override
    public void add(Order order) {

        //1:保存购物车 每一个购物车 商家为单位  一个购物车对应一个商家 对应一个订单
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("CART").get(order.getUserId());

        //实付金额 日志表的金额 (总金额)
        long tp = 0;
        //订单集合
        List<String> ids = new ArrayList<>();

        for (Cart cart : cartList) {
            //订单ID 唯一
            long id = idWorker.nextId();
            order.setOrderId(id);

            ids.add(String.valueOf(id));

            //总金额
            double totalPrice = 0;

            List<OrderItem> orderItemList = cart.getOrderItemList();
            for (OrderItem orderItem : orderItemList) {
                //订单详情对象 OrderItem

                //订单详情ID
                orderItem.setId(idWorker.nextId());
                //根据库存ID 查询库存对象
                Item item = itemDao.selectByPrimaryKey(orderItem.getItemId());
                //商品ID
                orderItem.setGoodsId(item.getGoodsId());
                //订单ID 外键
                orderItem.setOrderId(id);

                //标题
                orderItem.setTitle(item.getTitle());
                //单价
                orderItem.setPrice(item.getPrice());
                //小计
                orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue()*orderItem.getNum()));


                //计算此订单的总金额
                totalPrice += orderItem.getTotalFee().doubleValue();

                //图片
                orderItem.setPicPath(item.getImage());
                //商家ID
                orderItem.setSellerId(item.getSellerId());
                //保存订单详情表
                orderItemDao.insertSelective(orderItem);

            }
            //实付金额
            order.setPayment(new BigDecimal(totalPrice));
            //未付款
            order.setStatus("1");
            //创建时间
            order.setCreateTime(new Date());
            //更新时间
            order.setUpdateTime(new Date());
            //来源
            order.setSourceType("2");
            //商家ID
            order.setSellerId(cart.getSellerId());

            tp += order.getPayment().doubleValue()*100;

            //保存订单
            orderDao.insertSelective(order);

        }

        //保存日志表  (支付表)
        PayLog payLog = new PayLog();
        //ID
        payLog.setOutTradeNo(String.valueOf(idWorker.nextId()));

        //生成时间
        payLog.setCreateTime(new Date());
        //总金额 上面所有订单的金额
        payLog.setTotalFee(tp);

        //用户Id
        payLog.setUserId(order.getUserId());

        //交易状态
        payLog.setTradeState("0");
        //支付类型
        payLog.setPayType("1");
        //订单集合   "1,2,3,4,5"
        payLog.setOrderList(ids.toString().replace("[","").replace("]",""));

        //保存
        payLogDao.insertSelective(payLog);

        //保存缓存一份
        redisTemplate.boundHashOps("payLog").put(order.getUserId(),payLog);

        //清除购物车
        //redisTemplate.boundHashOps("CART").delete(order.getUserId());
        //删除购物车中已经购买的商品



    }


    //查询分页对象 条件
    @Override
    public PageResult search(Integer pageNum, Integer pageSize, Order order) {

        //分页小助手
        PageHelper.startPage(pageNum,pageSize);

        //条件查询
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();
        //订单号条件查询
        if (null!=order.getOrderId()&&!"".equals(order.getOrderId())){
            criteria.andOrderIdEqualTo(order.getOrderId());
        }
        //商家名称  模糊查询
        if (null!=order.getSellerId()&&!"".equals(order.getSellerId())){
            criteria.andSellerIdLike("%"+order.getSellerId().trim()+"%");
        }
        //收件人条件查询
        if (null!=order.getReceiver()&&!"".equals(order.getReceiver())){
            criteria.andReceiverLike(order.getReceiver().trim());
        }
        //查询所有
        Page<Order> page = (Page<Order>) orderDao.selectByExample(orderQuery);

        return new PageResult(page.getTotal(),page.getResult());
    }
    //订单发货
    @Override
    public List<OrderAndOrderItemVo> findOrderAndOrderItem(String name) {

        ArrayList<OrderAndOrderItemVo> list = new ArrayList<>();
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.createCriteria().andSellerIdEqualTo(name);
        List<Order> orderList = orderDao.selectByExample(orderQuery);

        for (Order order : orderList) {
            OrderAndOrderItemVo orderAndOrderItemVo = new OrderAndOrderItemVo();
            OrderItemQuery orderItemQuery = new OrderItemQuery();
            orderItemQuery.createCriteria().andOrderIdEqualTo(order.getOrderId());
            List<OrderItem> orderItemList = orderItemDao.selectByExample(orderItemQuery);
            orderAndOrderItemVo.setOrderId(order.getOrderId());
            orderAndOrderItemVo.setOrderItemList(orderItemList);
            list.add(orderAndOrderItemVo);
        }
        return list;


    }

    @Override
    public List<SellerOrderVo> findSellerOrder() {

        return orderDao.findXL();
    }

    //查询未支付订单
    public List<MyOrder> findOrderitemList(String name) {
        //获取登陆人用户名
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();
        criteria.andUserIdEqualTo(name);
        List<Order> orderList = orderDao.selectByExample(orderQuery);


        ArrayList<MyOrder> list = new ArrayList<>();//
        for (Order order : orderList) {
            MyOrder myOrder = new MyOrder();
            //未付款
            if (order.getStatus().equals("1")){
                myOrder.setCreateTime(order.getCreateTime()); //设置时间
                myOrder.setOrderId(order.getOrderId());       //设置订单ID
                myOrder.setSellerId(order.getSellerId());     //设置商家名称
                myOrder.setPostfee(order.getPostFee());      //邮费

                OrderItemQuery orderItemQuery = new OrderItemQuery();
                Long orderId = order.getOrderId();
                orderItemQuery.createCriteria().andOrderIdEqualTo(orderId);
                List<OrderItem> orderItems = orderItemDao.selectByExample(orderItemQuery);//OrderId 查询 orderitem集合
                myOrder.setOrderItemList(orderItems);

                list.add(myOrder);
            }
        }
        return list;
    }
    //查询全部订单
    public List<MyOrder> findAllOrder(String name) {
        //获取登陆人用户名
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();
        criteria.andUserIdEqualTo(name);
        List<Order> orderList = orderDao.selectByExample(orderQuery);


        ArrayList<MyOrder> list = new ArrayList<>();//
        for (Order order : orderList) {
            MyOrder myOrder = new MyOrder();
               //查询全部
                myOrder.setCreateTime(order.getCreateTime()); //设置时间
                myOrder.setOrderId(order.getOrderId());       //设置订单ID
                myOrder.setSellerId(order.getSellerId());     //设置商家名称
                myOrder.setPostfee(order.getPostFee());      //邮费

                OrderItemQuery orderItemQuery = new OrderItemQuery();
                Long orderId = order.getOrderId();
                orderItemQuery.createCriteria().andOrderIdEqualTo(orderId);
                List<OrderItem> orderItems = orderItemDao.selectByExample(orderItemQuery);//OrderId 查询 orderitem集合
                myOrder.setOrderItemList(orderItems);

                list.add(myOrder);
        }
        return list;
    }


}
