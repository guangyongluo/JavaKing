package com.vilin.geo.endpoint;

import com.vilin.geo.dto.DistanceDTO;
import com.vilin.geo.dto.LocationDTO;
import com.vilin.geo.dto.LocationNamesDTO;
import com.vilin.geo.service.GEOService;
import com.vilin.util.GsonUtil;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/geo")
public class GEOController {

  @Resource
  private GEOService geoService;

  @RequestMapping(value = "/addLocation", method = RequestMethod.POST)
  @ResponseBody
  public void addLocation(@RequestBody LocationDTO locationDTO) {
    geoService.addLocation(locationDTO.getName(), locationDTO.getLongitude(),
        locationDTO.getLatitude());
  }

  @RequestMapping(value = "/getLocation", method = RequestMethod.POST)
  @ResponseBody
  public String getLocationByName(@RequestBody LocationNamesDTO names) {
    final String[] array = names.getNames().toArray(new String[0]);
    final List<Point> positions = geoService.getPosition(array);
    return GsonUtil.gsonString(positions);
  }

  @RequestMapping(value = "/getLocationHash", method = RequestMethod.POST)
  @ResponseBody
  public String getLocationHashByName(@RequestBody LocationNamesDTO names) {
    final String[] array = names.getNames().toArray(new String[0]);
    final List<String> positionsHash = geoService.getPositionHash(array);
    return GsonUtil.gsonString(positionsHash);
  }

  @RequestMapping(value = "/calculateDistance", method = RequestMethod.POST)
  @ResponseBody
  public Double calculateDistance(@RequestBody DistanceDTO distanceDTO){
    return geoService.calculateDistance(distanceDTO.getFrom(), distanceDTO.getTo());
  }

  @RequestMapping(value = "/getNearByPoint", method = RequestMethod.POST)
  @ResponseBody
  public String getNearByPoint(@RequestBody LocationDTO locationDTO) {
    final List nearByPoint = geoService.getNearByPoint(locationDTO.getLongitude(),
        locationDTO.getLatitude(), locationDTO.getDistance());
    return GsonUtil.gsonString(nearByPoint);
  }

  @RequestMapping(value = "/getNearByName", method = RequestMethod.POST)
  @ResponseBody
  public String getNearByName(@RequestBody LocationDTO locationDTO) {
    final List nearByName = geoService.getNearByName(locationDTO.getName(),
        locationDTO.getDistance());
    return GsonUtil.gsonString(nearByName);
  }
}
