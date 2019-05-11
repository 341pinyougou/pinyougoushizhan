package cn.itcast.core.service;

import cn.itcast.core.pojo.item.ItemCat;
import entity.PageResult;
import entity.Result;
import vo.ItemCatListVo;

import java.util.List;
import java.util.Map;

public interface ItemCatService {
    List<ItemCat> findByParentId(Long parentId);

    ItemCat findOne(Long id);

    List<ItemCat> findAll();

    //分页查询
    PageResult search(Integer page,Integer rows);

    Result updateStatus(Long[] ids,String status);
}
