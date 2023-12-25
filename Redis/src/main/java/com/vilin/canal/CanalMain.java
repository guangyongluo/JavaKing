package com.vilin.canal;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import redis.clients.jedis.Jedis;

public class CanalMain {

  public static final Integer SECONDS = 60;

  public static final String CANAL_IP_ADDR = "127.0.0.1";

  private static void redisInsert(List<Column> columns)
  {
    JSONObject jsonObject = new JSONObject();
    for (Column column : columns)
    {
      System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
      jsonObject.put(column.getName(),column.getValue());
    }
    if(columns.size() > 0)
    {
      try(Jedis jedis = RedisUtils.getJedis())
      {
        jedis.set(columns.get(0).getValue(),jsonObject.toJSONString());
      }catch (Exception e){
        e.printStackTrace();
      }
    }
  }


  private static void redisDelete(List<Column> columns)
  {
    JSONObject jsonObject = new JSONObject();
    for (Column column : columns)
    {
      jsonObject.put(column.getName(),column.getValue());
    }
    if(columns.size() > 0)
    {
      try(Jedis jedis = RedisUtils.getJedis())
      {
        jedis.del(columns.get(0).getValue());
      }catch (Exception e){
        e.printStackTrace();
      }
    }
  }

  private static void redisUpdate(List<Column> columns)
  {
    JSONObject jsonObject = new JSONObject();
    for (Column column : columns)
    {
      System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
      jsonObject.put(column.getName(),column.getValue());
    }
    if(columns.size() > 0)
    {
      try(Jedis jedis = RedisUtils.getJedis())
      {
        jedis.set(columns.get(0).getValue(),jsonObject.toJSONString());
        System.out.println("---------update after: "+jedis.get(columns.get(0).getValue()));
      }catch (Exception e){
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {

    CanalConnector connector = CanalConnectors.newSingleConnector(
        new InetSocketAddress(CANAL_IP_ADDR, 11111), "example", "", "");

    // message batch size every connect to canal service.
    int batchSize = 1000;

    // empty count in message.
    int emptyCount = 0;

    try {

      connector.connect();
      connector.subscribe("test.t_user");
      connector.rollback();

      int totalEmptyCount = 10 * SECONDS;

      while (emptyCount < totalEmptyCount) {
        System.out.println("Canal客户端，每隔一秒监听bin_log的变化：" + UUID.randomUUID().toString());
        Message message = connector.getWithoutAck(batchSize);
        long batchId = message.getId();
        int size = message.getEntries().size();
        if (batchId == -1 || size == 0) {
          emptyCount++;
          System.out.println("empty count : " + emptyCount);
          try {
            TimeUnit.SECONDS.sleep(1);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        } else {
          emptyCount = 0;
          printEntry(message.getEntries());
        }

        connector.ack(batchId);
      }

      System.out.println("empty too many times, exit.");

    } finally {
      connector.disconnect();
    }

  }

  private static void printEntry(List<Entry> entries) {
    for (Entry entry : entries) {
      if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN
          || entry.getEntryType() == EntryType.TRANSACTIONEND) {
        continue;
      }

      RowChange rowChange = null;
      try {
        rowChange = RowChange.parseFrom(entry.getStoreValue());
      } catch (InvalidProtocolBufferException e) {
        throw new RuntimeException(
            "ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
            e);
      }

      EventType eventType = rowChange.getEventType();

      System.out.println(
          String.format("================>; binlog[%s:%s] , name[%s,%s] , eventType : %s",
              entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
              entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
              eventType));

      for(RowData rowData: rowChange.getRowDatasList()){
        if(eventType == EventType.DELETE) {
          printColumn(rowData.getBeforeColumnsList());
          redisDelete(rowData.getBeforeColumnsList());

        } else if (eventType == EventType.INSERT) {
          printColumn(rowData.getAfterColumnsList());
          redisInsert(rowData.getAfterColumnsList());
        } else {
          System.out.println("--------->: before");
          printColumn(rowData.getBeforeColumnsList());
          System.out.println("--------->：after");
          printColumn(rowData.getAfterColumnsList());
          redisUpdate(rowData.getAfterColumnsList());
        }
      }
    }
  }


  private static void printColumn(List<Column> columns) {
    for (Column column: columns) {
      System.out.println(column.getName() + " : " + column.getValue() + " update = " + column.getUpdated());
    }
  }
}
