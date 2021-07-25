package org.daas.bean;

public class RequestInfoBean {
	
	private String businessType;

	private String versionNo;

	private String caseId;

	private String caseCreateDate;

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getCaseCreateDate() {
		return caseCreateDate;
	}

	public void setCaseCreateDate(String caseCreateDate) {
		this.caseCreateDate = caseCreateDate;
	}

	@Override
	public String toString() {
		return "RequestInfoBean [businessType=" + businessType + ", ruleSetId=" + versionNo + ", caseId=" + caseId
				+ ", caseCreateDate=" + caseCreateDate + "]";
	}
}