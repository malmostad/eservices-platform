package  se.inherit.security.jaas.authify;
 

/**
 * +-------------------------------------------------------------------------------------+
 * |																					 |
 * | Demonstration code for Authify Client.  Runs only on JAVA		                	 |
 * |																					 |	
 * | Authify class make it faster and easier for users to access your website.			 |
 * | Handles the UI, authentication, and import user profile							 |
 * | and registration data for your website,											 |
 * | You can choose to request JSON data or other type of request format.				 |
 * |																					 |
 * | Instanciate Authify Client with your API key and your SECRET key. 					 |
 * | authifyClientRest authifyObject = new authifyClientRest(                            |
 * | "your_API_KEY", "your_SECRET_KEY", "http://your_server/");			 				 |
 * |                                                                                     |
 * | @author    Authify Team.                                                            |
 * |               								                                         |
 * +-------------------------------------------------------------------------------------+
 */

/* 
Modified Authify Client
Copyright (C) 2011 Inherit S AB

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.net.URLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class AuthifyClientRest 
{
	public static final String LOGIN_STATE = "login";
	private static Logger logger = Logger.getLogger(AuthifyClientRest.class.getName());
	
	private static String callbackurl;
	private static String api_key;
	private static String secret_key;
	
	public static String[] authifyServers = {"https://loginserver1.authify.com/",
				"https://loginserver1.authify.com/",
				"https://loginserver1.authify.com/",
				"https://loginserver1.authify.com/",
				"https://loginserver1.authify.com/",
				"https://loginserver1.authify.com/"
			   };
	
    public static int ServerUp = 0;
    private static String service;
	private static String v="8.4"; 
	private static String LUid;

	public AuthifyClientRest(String api_key, String secret_key, String callbackurl)
	{
		this.callbackurl = callbackurl;
        this.api_key = api_key;
        this.secret_key = secret_key;
		this.service = "require_login";
		this.LUid = "";
	}
	
	public static String RequireLoginLogException(String idp) {
		String result = null;
		try {
			result = RequireLogin(idp,"","");
		}
		catch (Exception e) {
			result = null;
			logger.severe("Failed to create login url: " + e);
		}
		return result;
	}
	
	/**
	* RequireLogin function
	*
	* Makes all the necessary requests to authenticate
	* the current user to the server.
	* @param String idp choosen by The current user.
	* @return String.
	*/
	public static String RequireLogin(String idp) throws Exception{
		return RequireLogin(idp,"","");
	}
	public static String RequireLogin(String idp, String loginparameterx, String localUserId) throws Exception
	{
		 String authify_request_token_hash;
    	 String authify_request_token = AuthifyClientRest.api_key + AuthifyClientRest.secret_key;
    	 
    	 authify_request_token_hash = getEncodedPassword(authify_request_token);
    	 
    	 HashMap<String,String> postelements ;
		 postelements = new HashMap<String,String>(); 
    
    	 postelements.put("api_key", AuthifyClientRest.api_key);
    	 postelements.put("uri", AuthifyClientRest.callbackurl);
    	 postelements.put("secret_key", AuthifyClientRest.secret_key);
    	 postelements.put("authify_request_token", authify_request_token_hash);
    	 postelements.put("idp", idp);
    	 postelements.put("luid", localUserId);
    	 postelements.put("loginparameters", loginparameterx);
    	 postelements.put("function", AuthifyClientRest.service);
	     postelements.put("v", AuthifyClientRest.v);
    	 
    	 String response = AuthifyClientRest.authifyrest_GetRequestStream_request_post(authifyServers[ServerUp] + "request/", postelements);
         return authifyServers[ServerUp] + "tokenidx.php?authify_request_token=" + authify_request_token_hash;
	}
	
	/**
    * Encode the string passed as a parameter with the MD5 algorithm
    *
    * @param String key  The String encoded
    * @return String value hexadecimal in 32 bits
    */
    public static String getEncodedPassword(String key) throws Exception
    {	 
	    byte[] uniqueKey = key.getBytes();
	    byte[] hash = null;
	    try 
	    {
	    	 hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
	    } 
	    catch (NoSuchAlgorithmException e) 
		{
	    	 throw new Error("no MD5 support in this VM");
	    }
	    StringBuffer hashString = new StringBuffer();
	    for ( int i = 0; i < hash.length; ++i ) 
		{
	   	   String hex = Integer.toHexString(hash[i]);
		   if ( hex.length() == 1 ) 
		   {
			  hashString.append('0');
			  hashString.append(hex.charAt(hex.length()-1));
		   } else 
		   {
			  hashString.append(hex.substring(hex.length()-2));
		   }
		}
	    return hashString.toString();    
     }
	
	/**
	 * RequireLogout function
	 *
	 * Terminates the current user's session,
	 * debugs the request and reset's the internal
	 * debudder.
	 *
	 * @return String.
	 */
	public static String RequireLogout(String authify_response_token)throws Exception
	{
        HashMap<String,String> parmsMap ;
		parmsMap = new HashMap<String,String>(); 
		parmsMap.put("authify_checksum", authify_response_token);
		parmsMap.put("v", AuthifyClientRest.v);
        return AuthifyClientRest.authifyrest_GetRequestStream_request_post(authifyServers[ServerUp] + "out/", parmsMap);
	}
	
	
	/**
	 * SendDataToAuthify function
	 *
	 * Send data format XML to authify sever
	 * use XML valid version 1.0
	 * @return boolean TRUE if the storing was terminated successfully, FALSE otherwise.
	 */	
	public static boolean SendDataToAuthify(String authify_response_token) throws Exception{
		return SendDataToAuthify("",authify_response_token);
	}
	
	
	public static boolean SendDataToAuthify(String xml,String authify_response_token) throws Exception
	{
		
    	 HashMap<String,String> postelements ;
		 postelements = new HashMap<String,String>(); 
    
		 postelements.put("extradata", xml);
		 postelements.put("secret_key", AuthifyClientRest.secret_key);
		 postelements.put("api_key", AuthifyClientRest.api_key);
		 postelements.put("authify_reponse_token", authify_response_token);    	     	
    	 postelements.put("function", "ExtradataProfiles");	
    	 postelements.put("v", AuthifyClientRest.v);
    	return !AuthifyClientRest.authifyrest_GetRequestStream_request_post(authifyServers[ServerUp] + "store/", postelements).isEmpty();    
	}
	
   /**
	* setSPUserId function
	*
	* Set the Service Provider User Id.
	* @param String localUserId 	The local user Id. 
	*/
	public void setSPUserId(String localUserId){
		this.LUid = localUserId;
	}
	
	/**
	* SetMapping 
	*/
	
	public void SetMapping(){
		this.service = "require_login_mapping";
	}
	
	/**
	* Get element of a noeud from xml file
	* @param String value
	* @return String result
	*/
	public static Object GetProperties(String value ,String authify_response_token) throws Exception
    {
		String resultat ="";
		  try {
			  
			  String xml = AuthifyClientRest.get_response("soap",authify_response_token) ; 
			  
			  System.out.println("XXXXXXXXXXXXXX authifyClientRest.get_response xml: [" + xml + "]");

                          DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			  Document doc = parser.parse(new InputSource(new StringReader(xml)));

			
			  doc.getDocumentElement().normalize();
			  NodeList nodeLst = doc.getElementsByTagName("nodeValue0");

			  for (int s = 0; s < nodeLst.getLength(); s++) { 
			
				Node fstNode = nodeLst.item(s);
				
				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
			  
				  Element fstElmnt = (Element) fstNode;
				  NodeList fstNmElmntLst = fstElmnt.getElementsByTagName(value);
				  Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
				  NodeList fstNm = fstNmElmnt.getChildNodes();
				  resultat = ((Node) fstNm.item(0)).getNodeValue();	
				}
			  }
		  } catch (Exception e) {
				e.printStackTrace();return "false";
		  }
		  
		  return resultat;
	}
	
	/**
	* get_response
	*
	* @param String.
	* @return String.
	*/
	
	public static String get_response(String format, String authify_response_token)throws Exception
    {
		HashMap<String,String> openWith ;
		openWith = new HashMap<String,String>(); 
    	openWith.put("authify_checksum", authify_response_token);
    	openWith.put("api_key", AuthifyClientRest.api_key);
    	openWith.put("uri", AuthifyClientRest.callbackurl);
    	openWith.put("secret_key", AuthifyClientRest.secret_key);
    	openWith.put("protocol", format);
    	openWith.put("v", AuthifyClientRest.v); 
        return AuthifyClientRest.authifyrest_GetRequestStream_request_post(authifyServers[ServerUp] + "json/", openWith);
     }
	
	/**
	 * authifyrest_GetRequestStream_request_post
	 *
	 * @param String.
	 * @param Map<String,String>.
	 * @return String.
	 */
	
	public static String authifyrest_GetRequestStream_request_post(String server ,Map<String,String>parmsMap) throws Exception
	{

	  URL url = new URL(server);
	  URLConnection connection = url.openConnection();
	  connection.setDoOutput(true);
	  OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
	  String post_="";

	  
	  for (Map.Entry<String,String> entry : parmsMap.entrySet()) 
	  {
		  post_+="&";
		  post_+=entry.getKey() + "=" + entry.getValue();
	  }

	
	 out.write(post_);

	  out.close();
	  BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	  String decodedString;
	  String decoded="";
	  while ((decodedString = in.readLine()) != null)
	  {
	     decoded=decoded+decodedString;
	  }
	  in.close();     
	 
	  return decoded;
	}

	/**
	 * Get A Html encode 
	 *
	 * @param String.
	 * @return String.
	 */
	private static final char c[] = { '<', '>', '&', '\"'};
	private static final String expansion[] = {"&lt;", "&gt;", "&amp;",
	                                     "&quot;"};
	public static String HTMLEncode(String s) {
	      StringBuffer st = new StringBuffer();
	      for (int i = 0; i < s.length(); i++) {
	          boolean copy = true;
	          char ch = s.charAt(i);
	          for (int j = 0; j < c.length ; j++) {
	            if (c[j]==ch) {
	                st.append(expansion[j]);
	                copy = false;
	                break;
	            }
	          }
	          if (copy) st.append(ch);
	      }
	      return st.toString();
	}
	

	
	
}