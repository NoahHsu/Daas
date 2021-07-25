package org.daas.bean;

public class ResponseInfoBean {

	private String returnCode;

	private String returnMessage;

	private String apiLogId;

	private String ruleSetId;

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

	public String getApiLogId() {
		return apiLogId;
	}

	public void setApiLogId(String apiLogId) {
		this.apiLogId = apiLogId;
	}

	public String getRuleSetId() {
		return ruleSetId;
	}

	public void setRuleSetId(String ruleSetId) {
		this.ruleSetId = ruleSetId;
	}

	@Override
	public String toString() {
		return "ResponseInfoBean [returnCode=" + returnCode + ", returnMessage=" + returnMessage + ", apiLogId="
				+ apiLogId + ", ruleSetId=" + ruleSetId + "]";
	}
	
}
