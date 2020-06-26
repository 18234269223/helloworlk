package com.personage.seckill.service;

import com.personage.seckill.model.Goods;

import java.util.List;

/**
 * ClassName:GoodsService
 * Package:com.personage.seckill.service
 * Description:
 *
 * @date:2020/6/26 15:18
 * @author:xxx
 */
public interface GoodsService {

    /**
     * 查询产品信息
     * @return
     */
    List<Goods> selectAllGoods();

    /**
     * 根据ID获取产品详情
     * @return
     */
    Goods getGoodsById(Integer goodsId);



}
