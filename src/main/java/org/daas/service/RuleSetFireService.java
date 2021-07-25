package org.daas.service;

import java.util.Map;
import org.daas.bean.RequestBodyBean;

public interface RuleSetFireService {
  /**
   * RuleCommonAPI入口、組ResponseInfo
   * 
   * @return
   * @throws ApiException
   */
  Map<String, Object> fireRules(String apiCode, RequestBodyBean requestBodyBean) throws Exception;
}
