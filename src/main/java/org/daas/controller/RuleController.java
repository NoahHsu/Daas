package org.daas.controller;

import org.daas.bean.KieBaseContainerBean;
import org.daas.bean.RequestBodyBean;
import org.daas.bean.ResponseBodyBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@Api(tags = "Test API")
@RequestMapping("/rule/")
public interface RuleController {

  @ApiOperation(value = "Fire")
  @PostMapping("/fire/{apiCode}")
  ResponseEntity<ResponseBodyBean> fireRule(@PathVariable("apiCode") String apiCode,
      @RequestBody RequestBodyBean requestBodyBean);

  @ApiOperation(value = "Build")
  @PostMapping("/build")
  ResponseEntity<String> buildRule(@RequestParam("apiCode") String apiCode,
      @RequestParam("version") String version);

  @ApiOperation(value = "Show Info")
  @PostMapping("/{apiCode}/{version}/info")
  ResponseEntity<KieBaseContainerBean> checkRule(@PathVariable("apiCode") String apiCode,
      @PathVariable("version") String version);
}
