package org.daas.bean.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.daas.utils.DateTimeUtils;

public class DaasServiceBean {
  private String category;
  private String code;
  private String version;
  private LocalDateTime effetiveDateTime;
  private LocalDateTime ineffetiveDateTime;
  private List<RuleGroupBean> rules = new ArrayList<>();
  private List<InputFcatModelBean> inputs = new ArrayList<>();
  private List<DefatulFactModelBean> outputs = new ArrayList<>();
  private List<DefatulFactModelBean> temps = new ArrayList<>();

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public LocalDateTime getEffetiveDateTime() {
    return effetiveDateTime;
  }

  public void setEffetiveDateTime(String effetiveDateTime) {
    this.effetiveDateTime = DateTimeUtils.toLocalDateTime(effetiveDateTime);
  }

  public LocalDateTime getIneffetiveDateTime() {
    return ineffetiveDateTime;
  }

  public void setIneffetiveDateTime(String ineffetiveDateTime) {
    this.ineffetiveDateTime = DateTimeUtils.toLocalDateTime(ineffetiveDateTime);
  }

  public List<RuleGroupBean> getRules() {
    return rules;
  }

  public void setRules(List<RuleGroupBean> rules) {
    this.rules = rules;
  }

  public List<InputFcatModelBean> getInputs() {
    return inputs;
  }

  public void setInputs(List<InputFcatModelBean> inputs) {
    this.inputs = inputs;
  }

  public List<DefatulFactModelBean> getOutputs() {
    return outputs;
  }

  public void setOutputs(List<DefatulFactModelBean> outputs) {
    this.outputs = outputs;
  }

  public List<DefatulFactModelBean> getTemps() {
    return temps;
  }

  public void setTemps(List<DefatulFactModelBean> temps) {
    this.temps = temps;
  }

}
