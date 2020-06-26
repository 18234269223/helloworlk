package com.personage.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.personage.seckill.mapper.GoodsMapper;
import com.personage.seckill.model.Goods;
import com.personage.seckill.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * ClassName:GoddsServiceImpl
 * Package:com.personage.seckill.service.impl
 * Description:
 *
 * @date:2020/6/26 15:12
 * @author:xxx
 */
@Service(interfaceClass = GoodsService.class)
@Component
public class GoddsServiceImpl implements GoodsService{

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private RedisTemplate redisTemplate;

    private StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    /**
     * 查询产品列表
     * @return
     */
    @Override
    public List<Goods> selectAllGoods() {
        return goodsMapper.selectAllGoods();
    }

    /**
     * 根据产品ID获取产品详情
     * @return
     */
    @Override
    public Goods getGoodsById(Integer goodsId) {
        return goodsMapper.selectByPrimaryKey(goodsId);
    }

}
