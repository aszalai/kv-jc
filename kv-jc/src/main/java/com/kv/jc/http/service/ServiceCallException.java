package com.kv.jc.http.service;

import com.google.common.base.Strings;

import okhttp3.Request;
import retrofit2.Call;

public class ServiceCallException extends Exception {
	private static final long serialVersionUID = -1656757022605669804L;
	private Call<?> call;

	public ServiceCallException(Call<?> call) {
		super(toMessage("", call));
		this.call = call;
		
	}

	public ServiceCallException(String message, Call<?> call) {
		super(toMessage(message, call));
		this.call = call;
		
	}

	public ServiceCallException(Call<?> call, Throwable cause) {
		super(toMessage("", call), cause);
		this.call = call;
		
	}

	public ServiceCallException(String message, Call<?> call, Throwable cause) {
		super(toMessage(message, call), cause);
		this.call = call;
		
	}

	public Call<?> getCall() {
		return call;
	}

	private static final String toMessage(String message, Call<?> call) {
		if (call == null || call.request() == null) {
			return "";
		}
		Request request = call.request();
		return "ERROR in " + request.method() + " " + request.url() + (Strings.isNullOrEmpty(message) ? "" : " - " + message);
	}
}
