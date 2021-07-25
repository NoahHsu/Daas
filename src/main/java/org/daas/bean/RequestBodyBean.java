package org.daas.bean;

import java.util.Map;

public class RequestBodyBean {

	private RequestInfoBean info;

	private Map<String, Object> data;

	public RequestInfoBean getInfo() {
		return info;
	}

	public void setInfo(RequestInfoBean info) {
		this.info = info;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "RequestBodyBean [info=" + info + ", data=" + data + "]";
	}

}