package org.daas.bean.config;

import java.math.BigDecimal;
import org.daas.bean.config.abs.DaasResourceBean;

public class RuleGroupBean extends DaasResourceBean {
  /**
   * default is null, as a ruleGroup that is passive
   */
  private Integer priority;

  public Integer getPriority() {
    BigDecimal.valueOf(150).multiply(BigDecimal.valueOf(0.7)).doubleValue();
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  @Override
  public String toString() {
    return priority + ": " + super.toString();
  }
}
