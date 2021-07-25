package org.daas.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.kie.api.KieBase;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class KieBaseContainerBean {
  @JsonIgnore
  private KieBase kiebase;
  private List<String> defaultRuleGroup = new ArrayList<>();
  private Map<String, List<String>> notNullInputFields = new HashMap<>();
  private Map<String, Integer> ansFactDefaultNum = new HashMap<>();
  private Map<String, Integer> tempFactDefaultNum = new HashMap<>();

  public KieBase getKiebase() {
    return kiebase;
  }

  public void setKiebase(KieBase kiebase) {
    this.kiebase = kiebase;
  }

  public List<String> getDefaultRuleGroup() {
    return defaultRuleGroup;
  }

  public void setDefaultRuleGroup(List<String> defaultRuleGroup) {
    this.defaultRuleGroup = defaultRuleGroup;
  }

  public Map<String, List<String>> getNotNullInputFields() {
    return notNullInputFields;
  }

  public void setNotNullInputFields(Map<String, List<String>> notNullInputFields) {
    this.notNullInputFields = notNullInputFields;
  }

  public Map<String, Integer> getAnsFactDefaultNum() {
    return ansFactDefaultNum;
  }

  public void setAnsFactDefaultNum(Map<String, Integer> ansFactDefaultNum) {
    this.ansFactDefaultNum = ansFactDefaultNum;
  }

  public Map<String, Integer> getTempFactDefaultNum() {
    return tempFactDefaultNum;
  }

  public void setTempFactDefaultNum(Map<String, Integer> tempFactDefaultNum) {
    this.tempFactDefaultNum = tempFactDefaultNum;
  }


}
