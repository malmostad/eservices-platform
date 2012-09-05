package se.inherit.security.jaas.authify;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

/*
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

public class AuthifyServlet extends HttpServlet {

	private static final long serialVersionUID = 6638940471400496219L;
	
	public static final String DEFAULT_LANDING_PAGE = ServerConfig.getConfig().getAuthifyLandingPage(); //"http://localhost:8080/citizen/wicket/bookmarkable/se.inherit.bonita.citizen.LoginPage";
	private static Logger logger = Logger.getLogger(AuthifyServlet.class.getName());
	
	public static final String LANDING_PAGE_PARAM = "landingPage";
	
	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		
		String landingPage= DEFAULT_LANDING_PAGE + "?";
			
		String reqPage = request.getParameter(LANDING_PAGE_PARAM);
		if (reqPage != null && reqPage.trim().length()>0) {
			landingPage = DEFAULT_LANDING_PAGE + "?" + LANDING_PAGE_PARAM + "=" + URLEncoder.encode(reqPage, "UTF8") + "&";
		}
		
		AuthifyClientRest authClient = new AuthifyClientRest(ServerConfig.getConfig().getAuthifyAppId(),
				ServerConfig.getConfig().getAuthifySecretKey(), landingPage);

		
		logger.severe("=====================> from service idp=" + request.getParameter("idp") + " authify_response_token=" + request.getParameter("authify_response_token"));
		
		PrintWriter out = response.getWriter();
		
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		out.println("<title>Authify Client Demo</title>");
		out.println("</head>");
		out.println("<body>");
		if ("tolvan".equals(request.getParameter("idp"))) {
			out.println("<script>window.location =\"" + landingPage + "authify_response_token=tolvan\"</script>");
		}
		else {
			try {
				
				if( AuthifyClientRest.GetProperties("state",request.getParameter("authify_response_token")).equals("login"))
				{
					try{
						if(!request.getParameter("idp").equals(""))
						{
							out.println("<script>window.location =\"" + AuthifyClientRest.RequireLogin(request.getParameter("idp"))+ "\"</script>");
						}
					}catch(NullPointerException e){}
					
					out.println("You are not logged in.<br /><br />Login with:<br/>");
					
					String idp = (String)AuthifyClientRest.GetProperties("item",request.getParameter("authify_response_token"));
					String[] idpx = idp.split(",");
								 
					for (int i=0; i<idpx.length;i++){
						out.println("<a href=?idp=" + idpx[i] + ">" + idpx[i] + "</a><br/>");
					}
				}
			
				try{		
					if( request.getParameter("logout").equals("logout"))
					{
						AuthifyClientRest.RequireLogout(request.getParameter("authify_response_token"));
						out.println("<script>window.location =\"" + landingPage + "\"</script>");
					}
				}
				catch(NullPointerException e){
					logger.severe("NullPointerException" + e);
					
				}
				
				if(AuthifyClientRest.GetProperties("state",request.getParameter("authify_response_token")).equals("logout") )
				{
					out.println("You are logged in.<br/><br/>");
					out.println("<a href=\"" + landingPage + "\"authify_response_token=" + request.getParameter("authify_response_token") +"\">Citizen web</a><br />");
				}
			} catch (Exception e1) {
				logger.severe("Exception" + e1);
			}
		}
		out.println("</body>");
		out.println("</html>");
	}
	
}
