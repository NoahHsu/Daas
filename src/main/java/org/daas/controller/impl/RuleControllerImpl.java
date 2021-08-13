package org.daas.controller.impl;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.daas.bean.KieBaseContainerBean;
import org.daas.bean.RequestBodyBean;
import org.daas.bean.ResponseBodyBean;
import org.daas.bean.ResponseInfoBean;
import org.daas.controller.RuleController;
import org.daas.service.KieBaseBuildService;
import org.daas.service.KieBaseContainerService;
import org.daas.service.RuleSetFireService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RuleControllerImpl implements RuleController {

  private KieBaseContainerService kieBaseContainerService;
  private KieBaseBuildService kieBaseBuildService;
  private RuleSetFireService ruleSetFireService;

  public RuleControllerImpl(KieBaseBuildService kieBaseBuildService,
      KieBaseContainerService kieBaseContainerService, RuleSetFireService ruleSetFireService) {
    this.kieBaseBuildService = kieBaseBuildService;
    this.kieBaseContainerService = kieBaseContainerService;
    this.ruleSetFireService = ruleSetFireService;
  }

  @Override
  public ResponseEntity<ResponseBodyBean> fireRule(String apiCode,
      RequestBodyBean requestBodyBean) {
    ResponseEntity<ResponseBodyBean> response;
    ResponseBodyBean responseBodyBean = new ResponseBodyBean();
    ResponseInfoBean info = new ResponseInfoBean();

    try {
      Map<String, Object> data = ruleSetFireService.fireRules(apiCode, requestBodyBean);
      responseBodyBean.setData(data);
      response = ResponseEntity.ok().body(responseBodyBean);
    } catch (Exception e) {
      info.setReturnMessage(e.getMessage());
      responseBodyBean.setInfo(info);
      response = ResponseEntity.badRequest().body(responseBodyBean);
      e.printStackTrace();
    }
    return response;
  }

  @Override
  public ResponseEntity<KieBaseContainerBean> checkRule(String apiCode, String version) {
    String key = kieBaseBuildService.kieBaseKey(apiCode, version);
    Optional<KieBaseContainerBean> bean = kieBaseContainerService.getKieBase(key);
    if (bean.isPresent()) {
      return ResponseEntity.ok(bean.get());
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @Override
  public ResponseEntity<Map<String, KieBaseContainerBean>> checkRule() {
    Map<String, KieBaseContainerBean> kieBases =
        kieBaseContainerService.getKieBaseIdSet().stream().collect(Collectors
            .toMap(Function.identity(), id -> kieBaseContainerService.getKieBase(id).get()));
    return ResponseEntity.ok(kieBases);
  }

}
