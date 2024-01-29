package com.vilin.bitmap.controller;

import com.vilin.bitmap.service.CustomerService;
import com.vilin.entity.Customer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api("Bloom Filter implementation")
@RestController("/bitmap")
@Slf4j
public class CustomerController {

  @Resource
  private CustomerService customerService;

  @ApiOperation("add customer api")
  @PostMapping("/customer/add")
  public void addCustomer(@RequestBody Customer customer){
    customerService.addCustomer(customer);
  }

  @ApiOperation("search customer by id")
  @GetMapping("/customer/{id}")
  public Customer findCustomerById(@RequestParam Integer id){
    return customerService.getCustomerById(id);
  }

  @ApiOperation("search customer by id with bloom filter")
  @GetMapping("/customer/bloomfilter/{id}")
  public Customer findCustomerByWithBloomFilter(@RequestParam Integer id){
    return customerService.getCustomerByIdWithBloomFilter(id);
  }


}
