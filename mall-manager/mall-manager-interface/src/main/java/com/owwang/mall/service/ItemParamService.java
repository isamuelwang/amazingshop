package com.owwang.mall.service;

import com.owwang.mall.pojo.EasyUIDataGriResult;
import com.owwang.mall.pojo.MallResult;
import com.owwang.mall.pojo.TbItemParam;

public interface ItemParamService {

    /**
     * 查询商品规格列表
     *
     * @param page
     * @param rows
     * @return
     */
    EasyUIDataGriResult getItemParamList(Integer page, Integer rows);

    MallResult getItemParamByCid(Long cid);

    MallResult insertItemParam(TbItemParam itemParam);
}
