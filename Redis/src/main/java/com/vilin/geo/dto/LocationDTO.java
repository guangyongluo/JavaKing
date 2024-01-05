package com.vilin.geo.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationDTO {
  private String name;
  private Double longitude;
  private Double latitude;
  private Double distance;
}
