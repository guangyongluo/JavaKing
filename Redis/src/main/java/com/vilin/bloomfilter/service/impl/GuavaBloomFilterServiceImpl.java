package com.vilin.bloomfilter.service.impl;


import static com.vilin.contants.BloomFilterConstants.BLOOM_FILTER_SIZE;
import static com.vilin.contants.BloomFilterConstants.EXCLUSION_RANGE;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.vilin.bloomfilter.service.GuavaBloomFilterService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GuavaBloomFilterServiceImpl implements GuavaBloomFilterService {

  @Override
  public void testFalsePositiveProbability() {

    BloomFilter bloomFilter = BloomFilter.create(Funnels.integerFunnel(), BLOOM_FILTER_SIZE);

    IntStream.range(1, BLOOM_FILTER_SIZE).forEach(bloomFilter::put);

    List<Integer> list = new ArrayList<>();

    IntStream.range(BLOOM_FILTER_SIZE + 1, BLOOM_FILTER_SIZE + EXCLUSION_RANGE).filter(bloomFilter::mightContain).forEach(list::add);

    log.info("fpp list is {} and fpp is {}.", list, (double) list.size() / BLOOM_FILTER_SIZE);




  }
}
