package com.vilin.bitmap.service.impl;

import static com.vilin.contants.RedisConstants.BLOOM_FILTER;
import static com.vilin.contants.RedisConstants.CUSTOMER_PREFIX;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vilin.bitmap.service.CustomerService;
import com.vilin.entity.Customer;
import com.vilin.mapper.CustomerMapper;
import com.vilin.util.BloomCheckUtil;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author luowei
 * @description 针对表【t_customer】的数据库操作Service实现
 * @createDate 2024-01-27 12:06:57
 */
@Service
@Slf4j
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer>
    implements CustomerService {

  @Resource
  private RedisTemplate redisTemplate;

  @Resource
  private BloomCheckUtil bloomCheckUtil;

  public void addCustomer(Customer customer) {

    final boolean save = this.save(customer);

    if (save) {
      final Customer result = this.getById(customer.getId());
      String key = CUSTOMER_PREFIX + result.getId();
      redisTemplate.opsForValue().set(key, result);
    }
  }

  public Customer getCustomerById(Integer id) {

    Customer customer = null;

    String key = CUSTOMER_PREFIX + id;

    customer = (Customer) redisTemplate.opsForValue().get(key);

    if (customer == null) {
      customer = this.getById(id);
      if (customer != null) {
        redisTemplate.opsForValue().set(key, customer);
      }
    }

    return customer;
  }

  public List<Integer> getAllCustomerIds() {
    final List<Customer> customerList = this.list();
    return customerList.stream().map(Customer::getId)
        .collect(Collectors.toList());
  }

  @Override
  public Customer getCustomerByIdWithBloomFilter(Integer id) {
    Customer customer = null;

    String key = CUSTOMER_PREFIX + id;

    customer = (Customer) redisTemplate.opsForValue().get(key);

    // add bloom filter
    if(!bloomCheckUtil.checkWhiteList(key, BLOOM_FILTER)){
      log.info("the key is not exists in white list.");
      return customer;
    }

    if (customer == null) {
      customer = this.getById(id);
      if (customer != null) {
        redisTemplate.opsForValue().set(key, customer);
      }
    }

    return customer;
  }


}




