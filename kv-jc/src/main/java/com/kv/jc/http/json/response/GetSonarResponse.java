package com.kv.jc.http.json.response;

import java.util.List;

import com.kv.jc.http.json.Entity;

public class GetSonarResponse extends AbstractResponse {

	private List<Entity> entities;

	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

}
