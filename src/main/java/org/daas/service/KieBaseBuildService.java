package org.daas.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.daas.bean.KieBaseContainerBean;
import org.daas.bean.config.DaasServiceBean;
import org.kie.api.KieBase;

public interface KieBaseBuildService {
  KieBase buildKieBase(DaasServiceBean serviceDef) throws FileNotFoundException, IOException;

  KieBaseContainerBean extractRuntimeInfo(DaasServiceBean serviceDef, KieBase kieBase);

  String kieBaseKey(String apiCode, String version);

}
