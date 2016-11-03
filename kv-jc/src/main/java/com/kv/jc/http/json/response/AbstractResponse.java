package com.kv.jc.http.json.response;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public abstract class AbstractResponse {

	protected static final Map<Integer, String> ERROR_MESSAGES = ImmutableMap.<Integer, String> builder()
			.put(1, "Nincs a csapat megh�vva")
			.put(2, "Folyamatban l�v� j�t�k")
			.put(3, "Nem l�tez� gameId")
			.put(4, "Nincs a csapatnak jogosults�ga a megadott tengeralattj�r�t kezelni")
			.put(7, "A torped� cooldownon van")
			.put(8, "�jrat�lt�d�s el�tti h�v�s")
			.put(9, "A j�t�k nincs folyamatban")
			.put(10, "A megadott haj� m�r mozgott ebben a k�rben")
			.put(11, "T�l nagy gyorsul�s")
			.put(12, "T�l nagy kanyarod�s")
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
