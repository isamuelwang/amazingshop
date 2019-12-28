package com.owwang.mall.item.pojo;

import com.owwang.mall.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

/**
 * @Classname ItemPojo
 * @Description TODO
 * @Date 2019-12-29
 * @Created by WANG
 */
public class ItemPojo extends TbItem {
    //构造方法
    public ItemPojo(TbItem item){
        BeanUtils.copyProperties(item,this);
    }

    //把图片String格式转化为String数组格式
    public String[] getImages(){
        if(StringUtils.isNotBlank(super.getImage())){
            return super.getImage().split(",");
        }
        return null;
    }
}
