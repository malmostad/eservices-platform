package org.inheritsource.taskform.engine;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.naming.*;
import javax.naming.directory.*;

import org.inheritsource.service.common.domain.UserDirectoryEntry;
import org.inheritsource.service.common.util.ConfigUtil;

public class UserDirectoryService {
	public static final Logger log = Logger.getLogger(UserDirectoryService.class
			.getName());

	private String host 					= null;
	private String port 					= null;
	private String protocol 				= null;
	private String pwd						= null;
	private String securityPrincipal	    = null;
	private String queryBaseDn 				= null; // concatenate with baseDN as resulting baseDn for query
	private String baseDn 	        		= null; 
	private String keystorePwd          	= null;

	private final int CN=0;
	private final int SN=1;
	private final int MAIL=2;
	private final int GIVENNAME=3;
	private final int DEPARTMENT=4;
	private final int COMPANY=5;
	
	private final String[] filterComponentArray = {
			// the indices must match the order in the filterArgs array
			"(cn=*{0}*)",
			"(sn=*{1}*)",
			"(mail=*{2}*)",
			"(givenName=*{3}*)",
			"(department=*{4}*)",
			"(company=*{5}*)"
	};

	/*	
	private String filterExprMalmo2 = "(&((objectClass=inetOrgPerson)" + 
			"(cn=*{0}*)(sn=*{1}*)(mail=*{2*)(givenName=*{3}*)" + 
			"(department=*{4}*)(company=*{5}*)))";
	*/
	
	private Hashtable<String, String> env = null;
	private DirContext dirCtx 		= null;
	private String filterExpr;

	public UserDirectoryService() {
		host        	  = ConfigUtil.getConfigProperties().getProperty("userDirectoryService.host");
		port        	  = ConfigUtil.getConfigProperties().getProperty("userDirectoryService.port");
		protocol    	  = ConfigUtil.getConfigProperties().getProperty("userDirectoryService.protocol");
		pwd           	  = ConfigUtil.getConfigProperties().getProperty("userDirectoryService.pwd");
		securityPrincipal = ConfigUtil.getConfigProperties().getProperty("userDirectoryService.securityPrincipal");
		queryBaseDn 	  = ConfigUtil.getConfigProperties().getProperty("userDirectoryService.queryBaseDn");
		baseDn      	  = ConfigUtil.getConfigProperties().getProperty("userDirectoryService.baseDn");
		keystorePwd  	  = ConfigUtil.getConfigProperties().getProperty("userDirectoryService.keystorePwd");

		System.out.println("=========================================================================================================" );
		System.out.println("host: [" + host + "]" );
		System.out.println("port: [" + port + "]" );
		System.out.println("protocol:[" + protocol + "]" );
		System.out.println("queryBaseDn: [" + queryBaseDn + "]" );
		System.out.println("baseDn: [" + baseDn + "]" );
		System.out.println("keystorePwd: [" + keystorePwd + "]" );
		System.out.println("pwd: [" + pwd + "]" );
		System.out.println("securityPrincipal: [" + securityPrincipal + "]" );
		System.out.println("=========================================================================================================" );


		// Set up initial dirContext
		env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, protocol + "://" + host + ":" + port);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		//env.put(Context.SECURITY_PROTOCOL, "ssl");
		env.put(Context.SECURITY_PRINCIPAL,      securityPrincipal);
		env.put(Context.SECURITY_CREDENTIALS,    pwd);
		try {
			if (queryBaseDn != null && queryBaseDn.trim().length()>0 &&
					baseDn != null && baseDn.trim().length()>0) {
				// Get access to trusted cert store (for ldaps)
				System.setProperty("javax.net.ssl.trustStorePassword", keystorePwd);

				System.out.println("creating initital context");
				dirCtx = new InitialDirContext(env);
			}
			else {
				System.out.println("Missing baseDn or queryBaseDn. Skip creation of initial context.");
			}
		} catch (NamingException ne) {
			ne.printStackTrace();
		}
	}

	private String buildFilterExpr(String[] filterArgs) {
		String result = "";
		String prefix = "(&(";
		String postfix = "))";

		for (int i=0 ; i < filterArgs.length ; i++ ) {
			if (filterArgs[i] != null && !filterArgs[i].isEmpty()) {
				result += filterComponentArray[i];
			}
		}
		result = (result.isEmpty() ? result: prefix+result+postfix );
		return result;
	}
	
	public void printAttrs(Attributes attrs) {
		if (attrs != null) {
			/* Print each attribute */
			try {
				for (NamingEnumeration<?> attributeEnum = attrs.getAll(); attributeEnum.hasMore();) {
					Attribute attr = (Attribute) attributeEnum.next();
					System.out.println("attribute: " + attr.getID());

					/* print each value */
					for (NamingEnumeration<?> namingEnum = attr.getAll(); namingEnum.hasMore(); System.out
							.println("    value: " + namingEnum.next()))
						;
				}
			} catch (NamingException ne) {
				ne.printStackTrace();
			}
		}
	}

	public Attributes getAttributesForCn(String cn) throws NamingException {
		Attributes result = null;
		if ( dirCtx != null ) {
			result = dirCtx.getAttributes("cn=" + cn + "," + queryBaseDn + "," + baseDn);
		}
		else {
			System.out.println("Severe, getAttributes: dirCtx null");
			//log.("Severe, getAttributes: dirCtx null");
		}
		return result;
	}

	public Attribute getAttributeForCnByAttributeName(String cn, String attributeName) {
		Attribute result = null;
		try {
			Attributes attrs = getAttributesForCn(cn);
			if (attrs != null) {
				for ( NamingEnumeration<?> attributeEnum = attrs.getAll(); attributeEnum.hasMore(); ) {
					Attribute attr = (Attribute) attributeEnum.next();
					if ( attr.getID().equals(attributeName)) {
						result = attr;
						break;
					}
				}
			}
		} catch (NamingException ne) {
			// TODO handle better
			ne.printStackTrace();
		}
		return result;
	}

	// Require non null argument
	public Attribute getAttributeByName(Attributes attrs, String attributeName) {
		Attribute result = null;
		try {
			for ( NamingEnumeration<? extends Attribute> attributeEnum = attrs.getAll(); attributeEnum.hasMore(); ) {
				Attribute attr = (Attribute) attributeEnum.next();
				if ( attr.getID().equals(attributeName)) {
					result = attr;
					break;
				}
			}
		} catch (NamingException ne) {
			// TODO handle better
			ne.printStackTrace();
		}
		return result;
	}

	public ArrayList<UserDirectoryEntry> searchForUserEntries(String[] filterArgs) {
		String base = queryBaseDn + ", " + baseDn;
		ArrayList<UserDirectoryEntry> result = new ArrayList<UserDirectoryEntry>();
		SearchControls sc = new SearchControls();
		System.out.println("UserDirectoryService.searchForUserEntries:");

		filterExpr = buildFilterExpr(filterArgs);
		System.out.println("filterExpr: " + filterExpr);

		System.out.println("filterArgs");
		int i = 0;
		for (String str : filterArgs) {
			System.out.print("filterArgs[" + i + "]: ");
			System.out.println("[" + str + "]");
			i++;
		}

		try {
			for ( NamingEnumeration<SearchResult> namingEnum = dirCtx.search(base, filterExpr, filterArgs,sc);
					namingEnum.hasMore();) {
				SearchResult searchRes = (SearchResult) namingEnum.next();
				UserDirectoryEntry ue = new UserDirectoryEntry();
				ue.setCn(         getAttributeByName(searchRes.getAttributes(),"cn"));
				ue.setSn(         getAttributeByName(searchRes.getAttributes(),"sn"));
				ue.setMail(       getAttributeByName(searchRes.getAttributes(),"mail"));
				ue.setGivenName(  getAttributeByName(searchRes.getAttributes(),"givenName"));
				ue.setDepartment( getAttributeByName(searchRes.getAttributes(),"department"));
				ue.setCompany(    getAttributeByName(searchRes.getAttributes(),"company"));
				result.add(ue);
			}
		} catch (NamingException ne) {
			ne.printStackTrace();
		}
		return result;
	}

	public ArrayList<UserDirectoryEntry> lookupUserEntries(String[] cnList) {
		ArrayList<UserDirectoryEntry> result = new ArrayList<UserDirectoryEntry>();
		try {
			for (String cn : cnList ) {
				UserDirectoryEntry ue = new UserDirectoryEntry();
				Attributes attrs = getAttributesForCn(cn);
				ue.setCn(cn);
				ue.setGivenName(  getAttributeByName(attrs,"givenName"));
				ue.setSn(         getAttributeByName(attrs,"sn"));
				ue.setMail(       getAttributeByName(attrs,"mail"));
				ue.setDepartment( getAttributeByName(attrs,"department"));
				ue.setCompany(    getAttributeByName(attrs,"company"));
				result.add(ue);
			}
		} catch (NamingException ne) {
			ne.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) throws NamingException {
		// Usage: UserDirectoryService  <host> <attributename:attributevalue> [<attributename:attributevalue>]

		// Create initial context
		System.out.println("UserDirectoryService: Creating initial context...");
		UserDirectoryService queryInstance = new UserDirectoryService();

		/*
			String filterExpr = "(&((objectClass=organizationalPerson)" + 
	    	"(cn=*{0}*)(givenName=*{1}*)(sn=*{2}*)(mail=*{3}*)(department=*{4}*)))";
		*/


		//String[] filterArgs = { "bjoho", "Björn", "Rob" };
		String[] filterArgs = args;

		for ( int i = 0 ; i<filterArgs.length ; i++ ) {
			System.out.print("    Arg: "+ i + ": " + filterArgs[i]);
			System.out.println("");
		}

		ArrayList<UserDirectoryEntry> queryEntriesResult = queryInstance.searchForUserEntries(filterArgs);

		System.out.println("========================================================");

		for ( UserDirectoryEntry ue : queryEntriesResult) {
			ue.print();
		}

		System.out.println("========================================================");
		System.out.println("lookup result: ");

		String[] userList = {"tesetj","bjohol4"};

		ArrayList<UserDirectoryEntry> lookupEntriesResult = queryInstance.lookupUserEntries(userList);

		for ( UserDirectoryEntry ue : lookupEntriesResult) {
			ue.print();
		}

		// Close the context when we're done

		if (queryInstance.dirCtx != null ) {
			queryInstance.dirCtx.close();
		}
		else {
			System.out.println("dirCtx null...");
		}
	}
}
