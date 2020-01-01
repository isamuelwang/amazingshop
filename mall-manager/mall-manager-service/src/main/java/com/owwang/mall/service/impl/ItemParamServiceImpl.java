package com.owwang.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.owwang.mall.mapper.TbItemCatMapper;
import com.owwang.mall.mapper.TbItemParamItemMapper;
import com.owwang.mall.mapper.TbItemParamMapper;
import com.owwang.mall.pojo.*;
import com.owwang.mall.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemParamServiceImpl implements ItemParamService {

    @Autowired
    private TbItemParamMapper tbItemParamMapper;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public EasyUIDataGriResult getItemParamList(Integer page, Integer rows) {
        //1.设置分页的信息
        if(page==null)page=1;
        if(rows==null)rows=30;
        PageHelper.startPage(page,rows);
        //2.注入mapper
        //3.查询
        TbItemParamExample example = new TbItemParamExample();
        List<TbItemParam> tbItemParams = tbItemParamMapper.selectByExampleWithBLOBs(example);
        //赋值商品分类名称
        for(TbItemParam tbItemParam : tbItemParams){
            TbItemCat itemCat = tbItemCatMapper.selectByPrimaryKey(tbItemParam.getItemCatId());
            tbItemParam.setItemCatName(itemCat.getName());
        }
        //4.取值
        PageInfo<TbItemParam> info = new PageInfo<TbItemParam>(tbItemParams);
        EasyUIDataGriResult result = new EasyUIDataGriResult();
        result.setTotal((int)info.getTotal());
        result.setRows(info.getList());
        //5.传值
        return result;
    }

    /**
     * 查询商品规格参数模板
     * @param cid
     * @return
     */
    @Override
    public MallResult getItemParamByCid(Long cid) {
        TbItemParamExample example = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = example.createCriteria();
        criteria.andItemCatIdEqualTo(cid);
        List<TbItemParam> tbItemParams = tbItemParamMapper.selectByExampleWithBLOBs(example);
        //查询到结果
        if(tbItemParams!=null&&tbItemParams.size()>0){
            return MallResult.ok(tbItemParams.get(0));
        }else{
            MallResult result = new MallResult();
            result.setMsg("当前商品没有设置规格");
            result.setStatus(400);
            return result;
        }
    }

    @Override
    public MallResult insertItemParam(TbItemParam itemParam) {
        //补全pojo
        itemParam.setCreated(new Date());
        itemParam.setUpdated(itemParam.getCreated());
        //添加信息
        tbItemParamMapper.insert(itemParam);
        return MallResult.ok();
    }
}
