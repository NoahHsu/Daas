package org.daas.service.impl;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.PostConstruct;
import org.daas.bean.KieBaseContainerBean;
import org.daas.bean.RequestInfoBean;
import org.daas.bean.config.DaasServiceBean;
import org.daas.config.ServiceConfig;
import org.daas.service.ApiConfigureService;
import org.daas.service.KieBaseBuildService;
import org.daas.service.KieBaseContainerService;
import org.daas.utils.DateTimeUtils;
import org.kie.api.KieBase;
import org.springframework.stereotype.Service;

@Service
public class ApiConfigureServiceImpl implements ApiConfigureService {

  private ServiceConfig serviceConfig;
  private KieBaseBuildService kieBaseBuildService;
  private KieBaseContainerService kieBaseContainerService;


  public ApiConfigureServiceImpl(ServiceConfig serviceConfig,
      KieBaseBuildService kieBaseBuildService, KieBaseContainerService kieBaseContainerService) {
    this.serviceConfig = serviceConfig;
    this.kieBaseBuildService = kieBaseBuildService;
    this.kieBaseContainerService = kieBaseContainerService;
  }


  @PostConstruct
  @Override
  public void preBuildRuleApi() throws Exception {
    List<DaasServiceBean> serviceDefs = serviceConfig.getServices();
    for (DaasServiceBean serviceDef : serviceDefs) {
      KieBase kieBase = kieBaseBuildService.buildKieBase(serviceDef);
      KieBaseContainerBean bean = kieBaseBuildService.extractRuntimeInfo(serviceDef, kieBase);
      String apikey = kieBaseBuildService.kieBaseKey(serviceDef.getCode(), serviceDef.getVersion());
      kieBaseContainerService.addKieBase(apikey, bean);
    }
  }


  @Override
  public DaasServiceBean findEffectiveService(String apiCode, RequestInfoBean info) {
    LocalDateTime targetDate = DateTimeUtils.toLocalDateTime(info.getCaseCreateDate());
    Predicate<DaasServiceBean> apiCodePred = e -> apiCode.equals(e.getCode());
    Predicate<DaasServiceBean> effectiveNoLimit = e -> e.getEffetiveDateTime() == null;
    Predicate<DaasServiceBean> afterEffective =
        e -> DateTimeUtils.isAfterOrEquals(targetDate, e.getEffetiveDateTime());
    Predicate<DaasServiceBean> ineffectiveNoLimit = e -> e.getEffetiveDateTime() == null;
    Predicate<DaasServiceBean> beforeEffective =
        e -> DateTimeUtils.isBeforeOrEquals(targetDate, e.getIneffetiveDateTime());
    Comparator<DaasServiceBean> effectiveDateCompare =
        (d1, d2) -> DateTimeUtils.compareSafely(d1.getEffetiveDateTime(), d2.getEffetiveDateTime());

    return serviceConfig.getServices().stream().filter(apiCodePred)
        .filter(effectiveNoLimit.or(afterEffective)).filter(ineffectiveNoLimit.or(beforeEffective))
        .sorted(effectiveDateCompare).findFirst().orElseThrow();
  }

}
