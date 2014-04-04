/* 
 *  Process Aware Web Application Platform 
 * 
 *  Copyright (C) 2011-2013 Inherit S AB 
 * 
 *  This program is free software: you can redistribute it and/or modify 
 *  it under the terms of the GNU Affero General Public License as published by 
 *  the Free Software Foundation, either version 3 of the License, or 
 *  (at your option) any later version. 
 * 
 *  This program is distributed in the hope that it will be useful, 
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 *  GNU Affero General Public License for more details. 
 * 
 *  You should have received a copy of the GNU Affero General Public License 
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 * 
 *  e-mail: info _at_ inherit.se 
 *  mail: Inherit S AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN 
 *  phone: +46 8 641 64 14 
 */ 
 
package org.inheritsource.service.identity;

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
