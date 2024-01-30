package com.vilin.bloomfilter.endpoint;

import com.vilin.bloomfilter.service.GuavaBloomFilterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api("Guava Bloom Filter implementation")
@RestController("/guava")
@Slf4j
public class GuavaBloomFilterController {

  @Resource
  private GuavaBloomFilterService guavaBloomFilterService;

  @ApiOperation("search customer by id with bloom filter")
  @GetMapping("/test")
  public void findCustomerByWithBloomFilter(){
    guavaBloomFilterService.testFalsePositiveProbability();
  }
}
