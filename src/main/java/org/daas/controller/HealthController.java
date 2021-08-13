package org.daas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@Api(tags = "Health")
@RequestMapping("/")
public interface HealthController {

  @ApiOperation(value = "health check")
  @GetMapping("health")
  ResponseEntity<String> healthCheck();

}
