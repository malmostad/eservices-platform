/* == Motrice Copyright Notice == 
 * 
 * Motrice Service Platform 
 * 
 * Copyright (C) 2011-2014 Motrice AB 
 * 
 * This program is free software: you can redistribute it and/or modify 
 * it under the terms of the GNU Affero General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU Affero General Public License for more details. 
 * 
 * You should have received a copy of the GNU Affero General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>. 
 * 
 * e-mail: info _at_ motrice.se 
 * mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 * phone: +46 8 641 64 14 
 
 */ 
 
package org.inheritsource.service.identity;

import java.util.Hashtable;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.*;
import javax.naming.directory.*;

import org.inheritsource.service.common.domain.UserDirectoryEntry;
import org.inheritsource.service.common.util.ConfigUtil;

public class UserDirectoryService {
	public static final Logger log = LoggerFactory.getLogger(UserDirectoryService.class
			.getName());

	private String host 					= null;
	private String port 					= null;
	private String protocol 				= null;
	private String pwd						= null;
	private String securityPrincipal	    = null;
	private String userBaseDn 				= null; // concatenate with baseDN as resulting baseDn for user query
    private String groupBaseDn 				= null; // concatenate with baseDN as resulting baseDn for group query
	private String baseDn 	        		= null; 
	private String keystorePwd          	= null;

	/*
	private final int CN=0;
	private final int SN=1;
	private final int MAIL=2;
	private final int GIVENNAME=3;
	private final int DEPARTMENT=4;
	private final int COMPANY=5;
	*/
	
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
		userBaseDn 	  = ConfigUtil.getConfigProperties().getProperty("userDirectoryService.userBaseDn");
		groupBaseDn 	  = ConfigUtil.getConfigProperties().getProperty("userDirectoryService.groupBaseDn");
		baseDn      	  = ConfigUtil.getConfigProperties().getProperty("userDirectoryService.baseDn");
		keystorePwd  	  = ConfigUtil.getConfigProperties().getProperty("userDirectoryService.keystorePwd");

		log.debug("=========================================================================================================" );
		log.debug("host: [" + host + "]" );
		log.debug("port: [" + port + "]" );
		log.debug("protocol:[" + protocol + "]" );
		log.debug("userBaseDn: [" + userBaseDn + "]" );
		log.debug("groupBaseDn: [" + groupBaseDn + "]" );
		log.debug("baseDn: [" + baseDn + "]" );
		log.debug("keystorePwd: [" + keystorePwd + "]" );
		log.debug("pwd: [" + pwd + "]" );
		log.debug("securityPrincipal: [" + securityPrincipal + "]" );
		log.debug("=========================================================================================================" );


		// Set up initial dirContext
		env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, protocol + "://" + host + ":" + port);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		//env.put(Context.SECURITY_PROTOCOL, "ssl");
		env.put(Context.SECURITY_PRINCIPAL,      securityPrincipal);
		env.put(Context.SECURITY_CREDENTIALS,    pwd);
		// TODO document this. 
                // Avoid setting  up LDAP machinery if LDAP parameters are non existant or empty
                // This should be configured more explicitely
		try {
			if (userBaseDn != null && userBaseDn.trim().length()>0 &&
					baseDn != null && baseDn.trim().length()>0) {
				// Get access to trusted cert store (for ldaps)
				System.setProperty("javax.net.ssl.trustStorePassword", keystorePwd);

				log.debug("creating initital context");
				dirCtx = new InitialDirContext(env);
			}
			else {
				log.info("Missing baseDn or userBaseDn. Skip creation of initial context.");
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
	
	private void printAttrs(Attributes attrs) {
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

	private Attributes getAttributesForCn(String cn) throws NamingException {
		Attributes result = null;
		if ( dirCtx != null ) {
			result = dirCtx.getAttributes("cn=" + cn + "," + userBaseDn + "," + baseDn);
		}
		else {
			log.info("Severe, getAttributes: dirCtx null");
			//log.("Severe, getAttributes: dirCtx null");
		}
		return result;
	}

	private Attributes getAttributesFromEntry(String dn) throws NamingException {
		Attributes result = null;
		if ( dirCtx != null ) {
			result = dirCtx.getAttributes(dn);
		}
		else {
			log.info("Severe, getAttributesFromEntry: dirCtx null");
			//log.("Severe, getAttributesFromEntry: dirCtx null");
		}
		return result;
	}

	public ArrayList<UserDirectoryEntry> lookupUserEntriesByGroup (String group) throws NamingException {

		ArrayList<UserDirectoryEntry> result = new ArrayList<UserDirectoryEntry>();
		ArrayList<String> userList = new ArrayList<String>();
		
		//System.out.println("group dn:" + "cn=" + group + "," + groupBaseDn + "," + baseDn);

		Attributes groupAttrs = getAttributesFromEntry("cn=" + group + "," + groupBaseDn + "," + baseDn);

		if (groupAttrs != null) {
			for ( NamingEnumeration<?> attributeEnum = groupAttrs.getAll(); attributeEnum.hasMore(); ) {
				Attribute attr = (Attribute) attributeEnum.next();
				if ( attr.getID().equals("member")) {
					for ( NamingEnumeration<?> memberEnum = attr.getAll(); memberEnum.hasMore(); ) {
						String memberStr = memberEnum.next().toString();
						//System.out.println("memberStr: " + memberStr);
						//System.out.println("memberUid: " + memberStr.substring(3,memberStr.indexOf(',')).trim());
						// extracting uid component of member attribute
						userList.add(memberStr.substring(3,memberStr.indexOf(',')).trim());
					}
				}
			}
		}
		for (String uidElement : userList) {
		    //System.out.println("uidElement: " + uidElement);
		    UserDirectoryEntry ude = lookupUserByCn(uidElement);
		    if ( ude != null) {
		    	//System.out.println("mail address" + ude.getMail());
		    	result.add(ude);
		    }
		}
		return result;
	}

	private Attribute getAttributeForCnByAttributeName(String cn, String attributeName) {
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
	private Attribute getAttributeByName(Attributes attrs, String attributeName) {
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
		String base = userBaseDn + ", " + baseDn;
		ArrayList<UserDirectoryEntry> result = new ArrayList<UserDirectoryEntry>();
		SearchControls sc = new SearchControls();
		log.debug("UserDirectoryService.searchForUserEntries:");

		filterExpr = buildFilterExpr(filterArgs);
		log.debug("filterExpr: " + filterExpr);

		log.debug("filterArgs");
		int i = 0;
		for (String str : filterArgs) {
			log.debug("filterArgs[" + i + "]: [" + str + "]");
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

	public ArrayList<UserDirectoryEntry> lookupUserEntries(String[] cnArray) {
		ArrayList<UserDirectoryEntry> result = new ArrayList<UserDirectoryEntry>();
		try {
			for (String cn : cnArray ) {
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

	public ArrayList<UserDirectoryEntry> lookupUserEntriesByGroupAndDepartmentNo(String groupName) {
		ArrayList<UserDirectoryEntry> result = new ArrayList<UserDirectoryEntry>();
/*
		try {
			for (String cn : cnArray ) {
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
		*/
		return result;
	}
	
	public UserDirectoryEntry lookupUserByCn(String cn) {
		UserDirectoryEntry result = null;
		Attributes attrs;
		try {
			attrs = getAttributesForCn(cn);
			result = new UserDirectoryEntry();
			result.setCn(cn);
			result.setGivenName(  getAttributeByName(attrs,"givenName"));
			result.setSn(         getAttributeByName(attrs,"sn"));
			result.setMail(       getAttributeByName(attrs,"mail"));
			result.setDepartment( getAttributeByName(attrs,"department"));
			result.setCompany(    getAttributeByName(attrs,"company"));
		} catch (NamingException ne) {
			log.error("lookupUserByCn, cn=" + cn + 
					" Exception: " + ne.toString());
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
		
		System.out.println("========================================================");
		System.out.println("Group lookup result: ");

		ArrayList<UserDirectoryEntry> groupMembers = queryInstance.lookupUserEntriesByGroup("Registrator");

		for ( UserDirectoryEntry ue : groupMembers) {
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
