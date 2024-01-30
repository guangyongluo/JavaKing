package com.vililn.guava.bloomfilter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;

public class GuavaBloomFilterTest {


  @Test
  public void testGuavaBloomFilter(){
    BloomFilter bloomFilter = BloomFilter.create(Funnels.longFunnel(), 100);

    assertThat(Boolean.FALSE, equalTo(bloomFilter.mightContain(1L)));
    assertThat(Boolean.FALSE, equalTo(bloomFilter.mightContain(2L)));

    bloomFilter.put(1L);
    bloomFilter.put(2L);

    assertThat(Boolean.TRUE, equalTo(bloomFilter.mightContain(1L)));
    assertThat(Boolean.TRUE, equalTo(bloomFilter.mightContain(2L)));
  }

}
