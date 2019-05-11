package cn.itcast.core.controller;


import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.ItemCatService;
import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vo.ItemCatListVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    @RequestMapping("/findItemCatList")
    public List<ItemCat> findItemCatList(Long parentId){
        return itemCatService.findByParentId(parentId);
    }

    @RequestMapping("/findItemCatList2")
    public List<ItemCatListVo> findItemCatList2(Long parentId){
        List<ItemCatListVo> list = new ArrayList<>();

        List<ItemCat> list2 = itemCatService.findByParentId(parentId);
        for (ItemCat itemCat : list2) {
            ItemCatListVo itemCatListVo = new ItemCatListVo();
            List<ItemCat> list3 = itemCatService.findByParentId(itemCat.getId());
            itemCatListVo.setList2(itemCat);
            itemCatListVo.setList3(list3);

            list.add(itemCatListVo);
        }
        return list;
    }


    /*@RequestMapping("/findAllItemCat")
    public List<ItemCatListVo> findAllItemCat(){

        List<ItemCatListVo> list = new ArrayList<ItemCatListVo>();

        List<ItemCat> list1 = itemCatService.findByParentId((long) 0);
        for (ItemCat itemCat1 : list1) {
            //创建包装对象
            ItemCatListVo itemCatListVo = new ItemCatListVo();
            //包装对象第一级填充
            itemCatListVo.setList1(itemCat1);
            //获取第一级id,作为下一级查询条件
            Long id1 = itemCat1.getId();
            //第二级目录
            List<ItemCat> list2 = itemCatService.findByParentId(id1);
            //TODO  把第二级目录放置在第一级目录下
            itemCatListVo.setList2(list2);
            //第三层目录
            for (ItemCat itemCat2 : list2) {
                Long id2 = itemCat2.getId();
                List<ItemCat> list3 = itemCatService.findByParentId(id2);
                //TODO  把第三级目录放置在第二级目录下
                itemCatListVo.setList3(list3);
            }
            //将结果放回到返回值中
            list.add(itemCatListVo);
        }
        return list;
    }*/



}
