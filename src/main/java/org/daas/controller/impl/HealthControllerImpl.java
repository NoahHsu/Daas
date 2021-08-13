package org.daas.controller.impl;

import org.daas.controller.HealthController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthControllerImpl implements HealthController {

  @Override
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok().body("ok");
  }

}
