package com.vilin.bitmap.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vilin.entity.Customer;
import java.util.List;

/**
* @author luowei
* @description 针对表【t_customer】的数据库操作Service
* @createDate 2024-01-27 12:06:57
*/

public interface CustomerService extends IService<Customer> {

  void addCustomer(Customer customer);

  Customer getCustomerById(Integer id);

  List<Integer> getAllCustomerIds();

  Customer getCustomerByIdWithBloomFilter(Integer id);

}
