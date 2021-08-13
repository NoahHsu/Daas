package org.daas.controller;

import java.util.Map;
import org.daas.bean.KieBaseContainerBean;
import org.daas.bean.RequestBodyBean;
import org.daas.bean.ResponseBodyBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@Api(tags = "Daas API")
@RequestMapping("/rules/")
public interface RuleController {

  @ApiOperation(value = "Show target version Info")
  @GetMapping("/{apiCode}/{version}/info")
  ResponseEntity<KieBaseContainerBean> checkRule(@PathVariable("apiCode") String apiCode,
      @PathVariable("version") String version);

  @ApiOperation(value = "Show all verison Info")
  @GetMapping("/info")
  ResponseEntity<Map<String, KieBaseContainerBean>> checkRule();

  @ApiOperation(value = "Fire")
  @PostMapping("/{apiCode}/fire")
  ResponseEntity<ResponseBodyBean> fireRule(@PathVariable("apiCode") String apiCode,
      @RequestBody RequestBodyBean requestBodyBean);

  // TODO
  // @ApiOperation(value = "Build")
  // @PostMapping("/build")
  // ResponseEntity<String> buildRule(@RequestParam("apiCode") String apiCode,
  // @RequestParam("version") String version);

}
