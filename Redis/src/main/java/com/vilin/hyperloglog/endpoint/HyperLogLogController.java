package com.vilin.hyperloglog.endpoint;

import com.vilin.hyperloglog.service.HyperLogLogService;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/redis")
@Controller
public class HyperLogLogController {

  @Resource
  private HyperLogLogService hyperLogLogService;


  @RequestMapping(value = "/page/uv", method = RequestMethod.GET)
  @ResponseBody
  @ApiOperation(value = "get current unique visitor count")
  public String getPageUV(){
    final Long pageUV = hyperLogLogService.getPageUV();
    return String.valueOf(pageUV);
  }

}
