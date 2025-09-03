package com.sap.cpi.test.securestore;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.sap.cpi.test.util.Parser;
import com.sap.cpi.test.util.RegisterService;
import com.sap.cpi.test.util.Utility;
import com.sap.it.api.securestore.SecureStoreService;

class SecureStoreServiceLoader implements Parser {

	@Override
	public void loadFile(String path) throws Exception {
		RegisterService.register(SecureStoreService.class.getName(), new SecureStoreServiceHandler());
				
		String  credentialAliasString  = Utility.readFilesToString(new File(path));
		
		String[] credentialAliasLines = credentialAliasString.split(System.lineSeparator());
		
		Arrays.stream(credentialAliasLines).forEach(K->{
			if(K.startsWith("Credential")||K.startsWith("SecureKey")) {
				String[] cred = K.split("\t");
				Map<String, String> usercredential = new HashMap<>();
				String name = cred[1];
				if(cred[0].equals("Credential"))
					usercredential.put("username", cred[2]);
				else
					usercredential.put("username", "");
				
				usercredential.put("password", cred[3]);
				
				for(int i=4;i<cred.length;i++) {
					usercredential.put("des"+(i-3), cred[i]);
				}
				SecureStoreServiceImpl.add(name, new UserCredentialImpl(usercredential));
			}
		});
		
	}

}
