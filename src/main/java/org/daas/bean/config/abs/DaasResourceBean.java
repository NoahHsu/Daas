package org.daas.bean.config.abs;

public class DaasResourceBean {
  private String code;
  private String packageName;
  private String version;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  @Override
  public String toString() {
    return packageName + "/" + code + "-" + version;
  }

}
