package org.daas.bean;

public class RequestInfoBean {

  private String ruleFireDateTime;

  public String getCaseCreateDate() {
    return ruleFireDateTime;
  }

  public void setRuleFireDateTime(String ruleFireDateTime) {
    this.ruleFireDateTime = ruleFireDateTime;
  }

  @Override
  public String toString() {
    return "RequestInfoBean [ruleFireDateTime=" + ruleFireDateTime + "]";
  }
}
