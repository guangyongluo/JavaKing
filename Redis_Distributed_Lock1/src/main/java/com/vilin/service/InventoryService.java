package com.vilin.service;

public interface InventoryService {

  String reduceInventory();

  String reduceInventoryWithDistributedLock();

  String reduceInventoryWithMutexLock();

  String reduceInventoryWithReentrantLock();

}
