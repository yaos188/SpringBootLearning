package com.thinkingcao.springboot.ehcache.service.impl;

import com.thinkingcao.springboot.ehcache.entity.Order;
import com.thinkingcao.springboot.ehcache.repository.OrderRepository;
import com.thinkingcao.springboot.ehcache.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @desc: order实现类
 * @author: cao_wencao
 * @date: 2019-12-11 14:14
 */
@Service
@Slf4j
@CacheConfig(cacheNames = {"userCache"})
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;


    @Cacheable(value = "order", key = "#orderId")
    @Override
    public Order findOne(Integer orderId) {
        log.info("【查询订单，缓存未找到，直接查询数据库，orderId为】：{}" , orderId);
        return this.orderRepository.findById(orderId).orElse(null);
    }

    @CachePut(value = "order", key = "#order.getOrderId()")
    @Override
    public void saveOne(Order order) {
        log.info("【新增订单，同步到缓存，orderId为】：{}" , order.getOrderId());
        this.orderRepository.save(order);
    }

    @CacheEvict(value = "order", key = "#orderId")
    @Override
    public void deleteOne(Integer orderId) {
        log.info("【删除订单，删除缓存，orderId为】：{}" , orderId);
        orderRepository.deleteById(orderId);
    }


}
