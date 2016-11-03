package com.kv.jc.http.json.response;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public abstract class AbstractResponse {

	protected static final Map<Integer, String> ERROR_MESSAGES = ImmutableMap.<Integer, String> builder()
			.put(1, "Nincs a csapat meghívva")
			.put(2, "Folyamatban lévõ játék")
			.put(3, "Nem létezõ gameId")
			.put(4, "Nincs a csapatnak jogosultsága a megadott tengeralattjárót kezelni")
			.put(7, "A torpedó cooldownon van")
			.put(8, "Újratöltõdés elõtti hívás")
			.put(9, "A játék nincs folyamatban")
			.put(10, "A megadott hajó már mozgott ebben a körben")
			.put(11, "Túl nagy gyorsulás")
			.put(12, "Túl nagy kanyarodás")
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
