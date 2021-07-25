package org.daas.bean.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.daas.bean.config.abs.DaasResourceBean;

public class InputFcatModelBean extends DaasResourceBean {
  private List<String> notNullFields = new ArrayList<>();

  public List<String> getNotNullFields() {
    return notNullFields;
  }

  public void setNotNullFields(List<String> notNullFields) {
    this.notNullFields = notNullFields;
  }

  @Override
  public String toString() {
    return super.toString() + ", notNullCheckFields: ["
        + notNullFields.stream().collect(Collectors.joining(", ")) + "]";
  }
}
