package com.kv.jc.http.json.response;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public abstract class AbstractResponse {

	protected static final Map<Integer, String> ERROR_MESSAGES = ImmutableMap.<Integer, String> builder()
			.put(1, "Nincs a csapat meghivva")
			.put(2, "Folyamatban levo jatek")
			.put(3, "Nem letezo gameId")
			.put(4, "Nincs a csapatnak jogosultsaga a megadott tengeralattjarot kezelni")
			.put(7, "A torpedo cooldownon van")
			.put(8, "Ujratoltodes elotti hivas")
			.put(9, "A jatek nincs folyamatban")
			.put(10, "A megadott hajo mar mozgott ebben a korben")
			.put(11, "Tul nagy gyorsulas")
			.put(12, "Tul nagy kanyarodas")
			.build();

	protected String message;
	protected Integer code;

	public boolean isOK() {
		return code == null || code == 0;
	}

	public final String getErrorMessage() {
		if (!isOK() && !ERROR_MESSAGES.containsKey(code)) {
			return "code: " + code;
		}
		return ERROR_MESSAGES.get(code);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
