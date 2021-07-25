package org.daas.service;

import org.daas.bean.RequestInfoBean;
import org.daas.bean.config.DaasServiceBean;

public interface ApiConfigureService {
  void preBuildRuleApi() throws Exception;

  DaasServiceBean findEffectiveService(String apiCode, RequestInfoBean info);

}
