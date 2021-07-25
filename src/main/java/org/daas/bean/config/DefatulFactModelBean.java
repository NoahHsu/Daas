package org.daas.bean.config;

import org.daas.bean.config.abs.DaasResourceBean;

public class DefatulFactModelBean extends DaasResourceBean {
  private int defaultNum;

  public int getDefaultNum() {
    return defaultNum;
  }

  public void setDefaultNum(int defaultNum) {
    this.defaultNum = defaultNum;
  }

}
