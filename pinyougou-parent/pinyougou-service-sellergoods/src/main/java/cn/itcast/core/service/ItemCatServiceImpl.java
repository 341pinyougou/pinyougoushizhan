package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import cn.itcast.core.pojo.item.ItemQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import vo.ItemCatListVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品分类管理
 */
@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {


    @Autowired
    private ItemCatDao itemCatDao;
    @Autowired
    private RedisTemplate redisTemplate;
    //根据父ID查询
    @Override
    public List<ItemCat> findByParentId(Long parentId) {

        //查询所有商品分类 保存缓存中  商品分类管理页面上 添加新的按钮  导入Mysql商品分类数据到缓存中
        List<ItemCat> itemCatList = findAll();
        for (ItemCat itemCat : itemCatList) {
            redisTemplate.boundHashOps("itemCat").put(itemCat.getName(),itemCat.getTypeId());
        }
        //正常查询  列表页面 显几个商品分类
        ItemCatQuery itemCatQuery = new ItemCatQuery();
        itemCatQuery.createCriteria().andParentIdEqualTo(parentId);
        return itemCatDao.selectByExample(itemCatQuery);
    }

    //查询一个
    @Override
    public ItemCat findOne(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }

    //查询所有商品分类结果集
    @Override
    public List<ItemCat> findAll() {
        return itemCatDao.selectByExample(null);
    }

    //分页查询
    public PageResult search(Integer page,Integer rows) {
        PageHelper.startPage(page,rows);

        ItemCatQuery itemCatQuery = new ItemCatQuery();
        ItemCatQuery.Criteria criteria = itemCatQuery.createCriteria();

        criteria.andStatusEqualTo("0");

        Page<ItemCat> p = (Page<ItemCat>) itemCatDao.selectByExample(itemCatQuery);
        return new PageResult(p.getTotal(),p.getResult());
    }

    //审核
    public Result updateStatus(Long[] ids,String status) {
        try {
            ItemCat itemCat = new ItemCat();

            if (null!=ids && ids.length>0) {
                for (Long id : ids) {
                    itemCat = itemCatDao.selectByPrimaryKey(id);
                    itemCat.setStatus(status);
                    itemCatDao.updateByPrimaryKey(itemCat);
                }
            }
            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }

    }


}
