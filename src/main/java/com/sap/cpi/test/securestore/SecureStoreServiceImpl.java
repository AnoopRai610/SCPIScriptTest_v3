package com.sap.cpi.test.securestore;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.CredentialNotFoundException;

import com.sap.it.api.securestore.AccessTokenAndUser;
import com.sap.it.api.securestore.SecureStoreService;
import com.sap.it.api.securestore.UserCredential;
import com.sap.it.api.securestore.exception.SecureStoreException;

class SecureStoreServiceImpl implements SecureStoreService {
	private static Map<String, UserCredential> userCredentials = new HashMap<>();
	private static Map<String, AccessTokenAndUser> accessTokenAndUsers = new HashMap<>();

	protected static void add(String name, UserCredential userCredential) {
		userCredentials.put(name, userCredential);
	}

	protected static void add(String name, AccessTokenAndUser accessTokenAndUser) {
		accessTokenAndUsers.put(name, accessTokenAndUser);
	}

	@Override
	public UserCredential getUserCredential(String var1) throws SecureStoreException {
		UserCredential userCredential = userCredentials.get(var1);
		if (userCredential == null)
			throw new SecureStoreException(
					new CredentialNotFoundException("User credential not found for " + var1));
		return userCredential;
	}

	@Override
	public AccessTokenAndUser getAccesTokenForOauth2AuthorizationCodeCredential(String arg0)
			throws SecureStoreException {
		AccessTokenAndUser accessTokenAndUser = accessTokenAndUsers.get(arg0);
		if (accessTokenAndUser == null)
			throw new SecureStoreException(
					new CredentialNotFoundException("oAuth security material not found for " + arg0));
		return accessTokenAndUser;
	}

	@Override
	public AccessTokenAndUser getAccesTokenForOauth2ClientCredential(String arg0) throws SecureStoreException {
		return getAccesTokenForOauth2AuthorizationCodeCredential(arg0);
	}

}
