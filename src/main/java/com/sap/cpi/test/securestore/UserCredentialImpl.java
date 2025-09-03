package com.sap.cpi.test.securestore;

import java.util.Map;
import java.util.stream.Collectors;

import com.sap.it.api.securestore.UserCredential;

class UserCredentialImpl implements UserCredential {
	private Map<String, String> usercredential;

	UserCredentialImpl(Map<String, String> usercredential) {
		this.usercredential = usercredential;
	}

	@Override
	public Map<String, String> getCredentialProperties() {
		return usercredential.entrySet().stream()
				.filter(e -> !(e.getKey().equals("username") || e.getKey().equals("password")))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@Override
	public char[] getPassword() {
		return usercredential.get("password").toCharArray();
	}

	@Override
	public String getUsername() {
		return usercredential.get("username");
	}

}
