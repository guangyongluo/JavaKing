package com.vilin.controller;

import com.vilin.service.InventoryService;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis/inventory")
public class RedisDistributedLockController {

  @Resource
  private InventoryService inventoryService;

  @GetMapping("/reduce")
  public String reduceInventory(){
    return inventoryService.reduceInventory();
  }

  @GetMapping("/reduce/distributed/lock")
  public String reduceInventoryWithDistributedLock(){
    return inventoryService.reduceInventoryWithDistributedLock();
  }

  @GetMapping("/reduce/mutex/lock")
  public String reduceInventoryWithMutexLock(){
    return inventoryService.reduceInventoryWithMutexLock();
  }

  @GetMapping("/reduce/reentrant/lock")
  public String reduceInventoryWithReentrantLock(){
    return inventoryService.reduceInventoryWithReentrantLock();
  }
}
