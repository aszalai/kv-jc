package com.kv.jc;

import java.io.IOException;

import com.kv.jc.http.service.ServiceController;

public class Main {

	public static final String BASE_URL = "http://195.228.45.100:8080/";

	public static void main(String[] args) throws IOException {
		ServiceController controller = ServiceController.create(BASE_URL);
		System.out.println(controller.getGames().size());
	}

}
