package org.daas.bean;

public class ResponseInfoBean {

  private String returnCode;

  private String returnMessage;

  public ResponseInfoBean() {
    super();
  }

  public String getReturnCode() {
    return returnCode;
  }

  public void setReturnCode(String returnCode) {
    this.returnCode = returnCode;
  }

  public String getReturnMessage() {
    return returnMessage;
  }

  public void setReturnMessage(String returnMessage) {
    this.returnMessage = returnMessage;
  }

  @Override
  public String toString() {
    return "ResponseInfoBean [returnCode=" + returnCode + ", returnMessage=" + returnMessage + "]";
  }

}
