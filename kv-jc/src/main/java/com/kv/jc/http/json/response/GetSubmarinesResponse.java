package com.kv.jc.http.json.response;

import java.util.List;

import com.kv.jc.http.json.Submarine;

public class GetSubmarinesResponse extends AbstractResponse {

	private List<Submarine> submarines;

	public List<Submarine> getSubmarines() {
		return submarines;
	}

	public void setSubmarines(List<Submarine> submarines) {
		this.submarines = submarines;
	}

}
