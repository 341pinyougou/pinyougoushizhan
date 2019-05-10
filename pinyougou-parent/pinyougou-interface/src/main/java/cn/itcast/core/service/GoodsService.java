package cn.itcast.core.service;

import cn.itcast.core.pojo.good.Goods;
import entity.PageResult;
import entity.Result;
import vo.GoodsVo;

import java.util.List;

public interface GoodsService {
    void add(GoodsVo vo);

    PageResult search(Integer page, Integer rows, Goods goods);

    GoodsVo findOne(Long id);

    void update(GoodsVo vo);

    void updateStatus(Long[] ids, String status);

    void delete(Long[] ids);

    Goods selectGoodsbyItemID(Long itemId);


    List<Goods> getScListToRedis();

    void addScListToRedis(List<Goods> shoucang);

}
