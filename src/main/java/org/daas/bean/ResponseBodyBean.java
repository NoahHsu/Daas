package org.daas.bean;

import java.util.Map;

public class ResponseBodyBean {

	private ResponseInfoBean info = new ResponseInfoBean();

	private Map<String, Object> data;

	public ResponseInfoBean getInfo() {
		return info;
	}

	public void setInfo(ResponseInfoBean info) {
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
		return "ResponseBodyBean [info=" + info + ", data=" + data + "]";
	}
	
}
