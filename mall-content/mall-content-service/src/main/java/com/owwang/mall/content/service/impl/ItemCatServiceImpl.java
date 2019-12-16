package com.owwang.mall.content.service.impl;

import com.owwang.mall.content.service.ItemCatService;
import com.owwang.mall.mapper.TbItemCatMapper;
import com.owwang.mall.pojo.EasyUITreeNode;
import com.owwang.mall.pojo.TbItemCat;
import com.owwang.mall.pojo.TbItemCatExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<EasyUITreeNode> getCatList(Long parentId) {
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
        List<EasyUITreeNode> result = new ArrayList<>();
        for(TbItemCat itemCat : list){
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setId(itemCat.getId());
            easyUITreeNode.setText(itemCat.getName());
            easyUITreeNode.setState(itemCat.getIsParent()?"closed":"open");
            result.add(easyUITreeNode);
        }
        //返回结果
        return result;
    }
}
