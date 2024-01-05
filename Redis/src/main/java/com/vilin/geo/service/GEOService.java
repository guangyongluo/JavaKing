package com.vilin.geo.service;


import java.util.List;
import javax.annotation.Resource;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands.GeoRadiusCommandArgs;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.Metrics;
import org.springframework.stereotype.Service;

@Service
public class GEOService {

  private final static String CITY_KEY = "city:beijin";

  @Resource
  private RedisTemplate redisTemplate;

  public void addLocation(String name, double longitude, double latitude) {
    Point point = new Point(longitude, latitude);
    redisTemplate.opsForGeo().add(CITY_KEY, point, name);
  }

  public List<Point> getPosition(String... names) {
    return redisTemplate.opsForGeo().position(CITY_KEY, names);
  }

  public List<String> getPositionHash(String... names) {
    return redisTemplate.opsForGeo().hash(CITY_KEY, names);
  }

  public Double calculateDistance(String form, String to) {
    final Distance distance = redisTemplate.opsForGeo()
        .distance(CITY_KEY, form, to, Metrics.METERS);
    return distance.getValue();
  }

  public List getNearByPoint(double longitude, double latitude, Double distance) {
    Point point = new Point(longitude, latitude);
    Circle circle = new Circle(point, new Distance(distance, Metrics.METERS));
    GeoRadiusCommandArgs args = GeoRadiusCommandArgs.newGeoRadiusArgs().includeCoordinates()
        .includeDistance().limit(10);
    final GeoResults geoResults = redisTemplate.opsForGeo().radius(CITY_KEY, circle, args);
    return geoResults.getContent();
  }

  public List getNearByName(String name, Double distance){
    GeoRadiusCommandArgs args = GeoRadiusCommandArgs.newGeoRadiusArgs().includeCoordinates()
        .includeDistance().limit(10);
    final GeoResults geoResults = redisTemplate.opsForGeo()
        .radius(CITY_KEY, name, new Distance(distance, Metrics.METERS), args);
    return geoResults.getContent();
  }

}
