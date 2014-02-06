package org.inheritsource.taskform.engine.directory;

import java.util.Hashtable;
import javax.naming.*;
import javax.naming.directory.*;

import org.inheritsource.service.common.util.ConfigUtil;

//    Usage: QueryForUserAttrs <host> <userid to lookup> <attribute>...

public class UserDirectoryService {

	private String host 			= null;
	private String port 			= null;
	private String protocol 		= null;
	private String pwd 				= null;
	private String securityPrincipal= null;
	private String queryBaseDn 		= null; // concatenate with baseDN as resulting baseDn for query
	private String baseDn 			= null; 

	private Hashtable<String, String> env = null;
	public DirContext dirCtx 		= null;

	public UserDirectoryService() {
		initConfig();
		// Set up initial dirContext
		this.env         = new Hashtable<String, String>();
		initDirContext();
	}

	private void initConfig() {
		host        		= ConfigUtil.getConfigProperties().getProperty("userDirectoryService.host");
		port        		= ConfigUtil.getConfigProperties().getProperty("userDirectoryService.port");
		protocol    		= ConfigUtil.getConfigProperties().getProperty("userDirectoryService.protocol");
		pwd           		= ConfigUtil.getConfigProperties().getProperty("userDirectoryService.pwd");
		securityPrincipal   = ConfigUtil.getConfigProperties().getProperty("userDirectoryService.securityPrincipal");
		queryBaseDn 		= ConfigUtil.getConfigProperties().getProperty("userDirectoryService.queryBaseDn");
		baseDn      		= ConfigUtil.getConfigProperties().getProperty("userDirectoryService.baseDn");
	}
	
	private void initDirContext() {
		// this must be present (ROL 2013-11-02)
		System.setProperty("javax.net.ssl.trustStorePassword",
						ConfigUtil.getConfigProperties().getProperty("userDirectoryService.keystorePwd"));

		env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, protocol + "://" + host + ":" + port);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL,      securityPrincipal);
		env.put(Context.SECURITY_CREDENTIALS,    pwd);
		try {
			dirCtx = new InitialDirContext(env);
		} catch (NamingException ne) {
			ne.printStackTrace();
		}
	} 

	public void printAttrs(Attributes attrs) {
		if (attrs == null) {
			System.out.println("No attributes");
		} else {
			/* Print each attribute */
			try {
				for (NamingEnumeration attributeEnum = attrs.getAll(); attributeEnum.hasMore();) {
					Attribute attr = (Attribute) attributeEnum.next();
					System.out.println("attribute: " + attr.getID());

					/* print each value */
					for (NamingEnumeration namingEnum = attr.getAll(); namingEnum.hasMore(); System.out
							.println("value: " + namingEnum.next()))
						;
				}
			} catch (NamingException ne) {
				ne.printStackTrace();
			}
		}
	}

	// Require non null argument
	public Attributes getAttributes(String uid) throws NamingException {
		Attributes result = dirCtx.getAttributes("cn=" + uid + "," + queryBaseDn + "," + baseDn);
		return result;
	}

	// Require non null argument
	public Attribute getAttribute(String uid, String name) {
		Attribute result = null;
		try {
			Attributes attrs = getAttributes(uid);
			if (attrs != null) {
				for ( NamingEnumeration attributeEnum = attrs.getAll(); attributeEnum.hasMore(); ) {
					Attribute attr = (Attribute) attributeEnum.next();
					if ( attr.getID().equals(name)) {
						result = attr;
						break;
					}
				}
			}
		} catch (NamingException ne) {
			// TODO handle better
			// ne.printStackTrace();
		}
		return result;
	}

	public String getEmailAddress(String userid) {
		String result = null;
		try {
			Attribute attr = getAttribute(userid,"mail");
			if (  attr != null ) {
				result = (String) attr.get();
			} else {
				result = "nil";
			}
		} catch (NamingException ne) {
			result = "nil";
		}
		return result;
	}

	public static void main(String[] args) throws NamingException {

		// Create initial context
		System.out.println("Creating initial context...");

		UserDirectoryService queryInstance = new UserDirectoryService();

		// System.out.println("Before dirCtx.lookup...");
		// Object obj = dirCtx.lookup("cn=" + userId + "," + queryBaseDn + "," baseDn);
		// do something useful with dirCtx

		//for (int i = 1; i < args.length; i++) {
		//    System.out.println("Attribute " + i + ": " + args[i]);
		//}

//		System.out.println("printAttrs: ");
//		queryInstance.printAttrs(queryInstance.getAttributes(args[1]));

		System.out.println("===========================================================================");

//		for (int i = 0; i < args.length; i++) {
//			Attribute curAttr = queryInstance.getAttribute(args[0], args[i]);
//			System.out.println("Attribute " + args[i] + ": " + ( curAttr == null ? "null": curAttr.get()));
//		}
			String mailaddr = queryInstance.getEmailAddress(args[0]);
			System.out.println("Mail address: " + mailaddr);

		// Close the context when we're done

		if (queryInstance.dirCtx != null ) {
			queryInstance.dirCtx.close();
		}
		else {
			System.out.println("dirCtx null...");
		}
	}
}
