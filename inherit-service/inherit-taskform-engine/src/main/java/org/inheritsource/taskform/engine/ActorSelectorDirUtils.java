package org.inheritsource.taskform.engine;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;


public class ActorSelectorDirUtils {

	private String hostName;
	private String port;
	private String baseDN;
	private Hashtable<String, String> env;

	public ActorSelectorDirUtils(String host, String port, String base) {
		setHostName(host);
		setPort(port);
		setBaseDN(base);
		env = new Hashtable<String, String>();

		env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://" + hostName + ":" + port);

		// Authenticate as user "Directory Manager" with password "0pen1dap"
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "cn=Directory Manager");
		env.put(Context.SECURITY_CREDENTIALS, "0pen1dap");
	}

	public void setHostName(String hn) {
		hostName=hn;
	}

	public void setPort(String p) {
		port=p;
	}

	public void setBaseDN(String bn) {
		baseDN=bn;
	}

	public String getHostname() {
		return hostName;
	}

	public String getPort() {
		return port;
	}

	public String getBaseDN() {
		return baseDN;
	}

	public Set<String> getUsersByDepartmentAndRole(String depmtName, String roleName) throws Exception {
		Set<String> resultArray = new HashSet<String> ();
		if (depmtName != null && roleName != null) {
			String base = getBaseDN();
			String[] attributeFilter = { "ou" };
			String depmtFilter = "(&(objectclass=organizationalunit)(ou=*" + depmtName + "*))";
			SearchControls searchControls = new SearchControls();
			searchControls.setReturningAttributes(attributeFilter);
			searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);

			System.out.println("depmtFilter: " + depmtFilter);
			try {
				System.out.println("Creating depmt initial context...");
				DirContext ctx = new InitialDirContext(env);
				System.out.println("Before first ctx.lookup...");

				NamingEnumeration results = ctx.search(base, depmtFilter, searchControls);

				while (results.hasMore()) {
					SearchResult sr = (SearchResult) results.next();
					Attributes   attrs = sr.getAttributes();
					Attribute    attr = attrs.get("ou");
					NamingEnumeration<?> ouEnum = attr.getAll();

					while (ouEnum.hasMore()) {
						String ou = ouEnum.next().toString();
						System.out.println("Ou: " + ou);
						base = "ou=" + ou + "," + base;
						System.out.println("New base: " + base);
					}
				}

				String[] attributeFilter2 = { "member" };
				searchControls.setReturningAttributes(attributeFilter2);
				searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

				String roleFilter = "(&(objectclass=groupOfEntries)(cn=" + roleName + "))";

				NamingEnumeration<SearchResult> roleResults = ctx.search(base, roleFilter, searchControls);

				while (roleResults.hasMore()) {
					SearchResult sr = roleResults.next();
					Attributes attrs = sr.getAttributes();
					Attribute attr = attrs.get("member");
					NamingEnumeration<?> memberEnum = attr.getAll();
					while (memberEnum.hasMore()) {
						Object member = memberEnum.next();
						//					System.out.println("Member:" + memberStr);
						resultArray.add(member.toString());
					}
				}
				ctx.close();
			} catch (Exception e) {}
		}
		return resultArray;
	}

	public static void main(String[] args) throws Exception {
		// Set up environment for creating initial context
		String hostname = args[0];
		String port = args[1];
		String depmtName = args[2];
		String roleName = args[3];
		String base = "ou=IDMGroups,OU=Organisation,OU=Malmo,DC=adm,DC=malmo,DC=se";
		ActorSelectorDirUtils aselectorUtils = new ActorSelectorDirUtils(hostname, port, base);

		try {
			Set<String> roleUsers =   
					aselectorUtils.getUsersByDepartmentAndRole(depmtName, roleName); 

			Iterator<String> it = roleUsers.iterator();
			while(it.hasNext())
			{
				String user = it.next();
				System.out.println("User in role " + roleName +": " + user);
			}
		} catch (Exception e) {
		}
	}
}
