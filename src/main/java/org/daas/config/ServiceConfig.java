package org.daas.config;

import java.util.List;
import org.daas.bean.config.DaasServiceBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dass-service")
public class ServiceConfig {
  private List<DaasServiceBean> services;

  public List<DaasServiceBean> getServices() {
    return services;
  }

  public void setServices(List<DaasServiceBean> services) {
    this.services = services;
  }


}
