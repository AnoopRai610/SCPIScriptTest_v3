package com.sap.cpi.test;

import java.io.File;
import java.io.IOException;

import org.codehaus.groovy.control.CompilationFailedException;

import com.sap.cpi.test.util.Parser;
import com.sap.cpi.test.util.Utility;
import com.sap.gateway.ip.core.customdev.util.Message;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

public class RunScript {
	Message message;
	Script script;

	/**
	 * Run Script with message body and headers loaded from file. In case if path is
	 * just a file then it will be loaded directly into body as InputStreamCache. In
	 * case of path is directory, then it will look for below files name contains
	 * and load to message:
	 * <ul>
	 * <li><b>body</b> - Load this file to body</li>
	 * <li><b>header</b> - Load this tab separated file to header</li>
	 * <li><b>property</b> - Load this tab separated file to property</li>
	 * <li><b>attachment</b> - Load files as attachment</li>
	 * </ul>
	 * 
	 * @param messagePath
	 * @param scriptPath
	 * @throws CompilationFailedException
	 * @throws IOException
	 */
	public RunScript(String messagePath, String scriptPath) throws CompilationFailedException, IOException {
		this(new File(messagePath), new File(scriptPath));
	}

	/**
	 * Run Script with message body and headers loaded from file. In case if path is
	 * just a file then it will be loaded directly into body as InputStreamCache. In
	 * case of path is directory, then it will look for below files name contains
	 * and load to message:
	 * <ul>
	 * <li><b>body</b> - Load this file to body</li>
	 * <li><b>header</b> - Load this tab separated file to header</li>
	 * <li><b>property</b> - Load this tab separated file to property</li>
	 * <li><b>attachment</b> - Load files as attachment</li>
	 * </ul>
	 * 
	 * @param messageFile
	 * @param scriptFile
	 * @throws CompilationFailedException
	 * @throws IOException
	 */
	public RunScript(File messageFile, File scriptFile) throws CompilationFailedException, IOException {
		this.message = new MessageImpl(messageFile);
		this.setScriptPath(scriptFile);
	}

	/**
	 * Run Script with empty message.
	 * 
	 * @param scriptPath
	 * @throws CompilationFailedException
	 * @throws IOException
	 */
	public RunScript(String scriptPath) throws CompilationFailedException, IOException {
		this(new File(scriptPath));
	}

	/**
	 * Run Script with empty message.
	 * 
	 * @param scriptFile
	 * @throws CompilationFailedException
	 * @throws IOException
	 */
	public RunScript(File scriptFile) throws CompilationFailedException, IOException {
		this.message = new MessageImpl();
		this.setScriptPath(scriptFile);
	}

	/**
	 * Load file to Message.
	 * 
	 * In case if path is just a file then it will be loaded directly into body as
	 * InputStreamCache. In case of path is directory, then it will look for below
	 * files name contains and load to message:
	 * <ul>
	 * <li><b>body</b> - Load this file to body</li>
	 * <li><b>header</b> - Load this tab separated file to header</li>
	 * <li><b>property</b> - Load this tab separated file to property</li>
	 * <li><b>attachment</b> - Load files as attachment</li>
	 * </ul>
	 * 
	 * @param messageFile
	 * @return {@link Message}
	 */
	public Message createMessage(File messageFile) {
		return this.message = new MessageImpl(messageFile);
	}

	/**
	 * Load file on provided path to Message.
	 * 
	 * In case if path is just a file then it will be loaded directly into body as
	 * InputStreamCache. In case of path is directory, then it will look for below
	 * files name contains and load to message:
	 * <ul>
	 * <li><b>body</b> - Load this file to body</li>
	 * <li><b>header</b> - Load this tab separated file to header</li>
	 * <li><b>property</b> - Load this tab separated file to property</li>
	 * <li><b>attachment</b> - Load files as attachment</li>
	 * </ul>
	 * 
	 * @param messagePath
	 * @return {@link Message}
	 */
	public Message createMessage(String messagePath) {
		return this.createMessage(new File(messagePath));
	}

	/**
	 * Get Message object
	 * 
	 * @return {@link Message}
	 */
	public Message getMessage() {
		return this.message;
	}

	/**
	 * Set script path that need to execute.
	 * 
	 * @param scriptFile
	 * @throws CompilationFailedException
	 * @throws IOException
	 */
	public void setScriptPath(File scriptFile) throws CompilationFailedException, IOException {
		GroovyShell shell = new GroovyShell();
		this.script = shell.parse(scriptFile);
		MessageLogFactory messageLogFactory = new MessageLogFactory();
		this.script.setProperty("messageLogFactory", messageLogFactory);
	}

	/**
	 * Load value mapping files download from SAP CPI.
	 * 
	 * @param vmFileName
	 * @throws Exception
	 */
	public void loadValuemapping(String vmFileName) throws Exception {
		Utility.getAccessToProtectedClass("com.sap.cpi.test.vm.ValueMappingApiLoader", Parser.class)
				.loadFile(vmFileName);
	}

	/**
	 * Load user credential from Tab separated file, format need be as below.
	 * 
	 * <blockquote> For Credential: Credential CredentialName User Password
	 * 
	 * For Security Key SecurityKey SecurityKeyName Key </blockquote>
	 * 
	 * <i>Currently it supports only Credential and Security key, not Access Toke/i>
	 * 
	 * @param secureFileName
	 * @throws Exception
	 */
	public static void loadUserCredential(String secureFileName) throws Exception {
		Utility.getAccessToProtectedClass("com.sap.cpi.test.securestore.SecureStoreServiceLoader", Parser.class)
				.loadFile(secureFileName);
	}

	/**
	 * Invoke <em>processData</em> method in Groovy script.
	 * 
	 * @return {@link Message}
	 */
	public Message invokeMethod() {
		return this.invokeMethod("processData");
	}

	/**
	 * Invoke <em>provided method name</em> in Groovy script.
	 * 
	 * @param methodName
	 * @return {@link Message}
	 */
	public Message invokeMethod(String methodName) {
		MessageImpl messageImpl = (MessageImpl) this.message;
		messageImpl.loadDataToExchange();

		return this.message = (Message) this.script.invokeMethod(methodName, this.message);
	}
}