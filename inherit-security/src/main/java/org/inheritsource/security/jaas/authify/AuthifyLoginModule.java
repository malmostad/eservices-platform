package org.inheritsource.security.jaas.authify;

import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.security.auth.Destroyable;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthifyLoginModule implements LoginModule {

	public static final Logger log = Logger.getLogger(AuthifyLoginModule.class
			.getName());

	public static final String DEBUG_OPTION_NAME = "debug";

	private Subject subject;
	private CallbackHandler callbackHandler;
	private Map<String, Object> sharedState;

	private boolean debug = false;
	private String id;
	private String authifyResponseToken;

	@Override
	public boolean abort() throws LoginException {
		if (this.debug) {
			System.err.println("abort()");
		}
		if (this.id == null) {
			return false;
		}
		this.subject = null;
		this.id = null;
		this.authifyResponseToken = null;
		return true;
	}

	@Override
	public boolean commit() throws LoginException {
		if (this.id == null) {
			String msg = "jaas-commit: Failed to login user";
			log.info(msg);
			throw new FailedLoginException("AuthifyBonitaLogiSet<E>dule " + msg);
		}

		final Set<Principal> principals = this.subject.getPrincipals();

		// principals.add(...);

		return true;
	}

	@Override
	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map<String, ?> sharedState, Map<String, ?> options) {
		// TODO Auto-generated method stub
		log.info("subject: " + subject);
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.sharedState = (Map<String, Object>) sharedState;

		// initialize any configured options
		final String debugFlag = (String) options.get(DEBUG_OPTION_NAME);
		if (debugFlag != null) {
			this.debug = Boolean.valueOf(debugFlag);
		}

	}

	@Override
	public boolean login() throws LoginException {
		if (debug) {
			log.info("login preparing step 1 - callback authify ticket");
		}

		String authifyTicket = callbackAuthifyTicket();

		if (debug) {
			log.info("login preparing step 2 - retrieve user data with authify ticket: "
					+ authifyTicket);
		}

		String bonitaUserName = null;
		if (authifyTicket == null || authifyTicket.trim().length() == 0) {

			// bonitaUserName = noAuthifyTicketLogin();
		} else {
			bonitaUserName = retrieveUserDataToSharedState(authifyTicket);
		}

		if (debug) {
			log.info("login preparing step 3 - verify bonita user with id: "
					+ bonitaUserName);
		}

		boolean userOk = (bonitaUserName != null && bonitaUserName.trim()
				.length() > 0);

		// boolean bonitaUserOk = verifyBonitaUser(bonitaUserName);

		if (debug) {
			log.info("login result: " + userOk);
		}

		System.out.println("LOGIN =======> " + this.authifyResponseToken
				+ " : bonitaUserOk=" + userOk);

		return userOk;
	}

	@Override
	public boolean logout() throws LoginException {
		log.info("JAAS logout");

		if (this.id != null) {
			if (this.debug) {
				log.info("logout() - removing principals");
			}

			final Set<Principal> principals = new HashSet<Principal>(
					this.subject.getPrincipals());
			for (final Principal p : principals) {
				// remove?
			}

			if (this.debug) {
				log.info("logout() - destroying/removing credentials");
			}

			// Remove/destroy only credentials added by our commit method
			final Set<Object> credentials = new HashSet<Object>(
					this.subject.getPublicCredentials());
			for (final Object o : credentials) {
				if (o instanceof Destroyable) {
					if (this.debug) {
						log.info("logout() - destroying credential: " + o);
					}
				}
				if (!this.subject.isReadOnly()) {
					if (this.debug) {
						log.info("logout() - removing credential: " + o);
					}
					this.subject.getPublicCredentials().remove(o);
				}
			}
		}
		this.authifyResponseToken = null;

		return true;
	}

	private String callbackAuthifyTicket() throws LoginException {
		if (this.debug) {
			log.info("callbackAuthifyTicket: retrieve authify ticket");
		}
		String result = null;

		/*
		 * Read Authify ticket from Callback to the authifyResponseToken
		 * attribute
		 */
		AuthifyCallback authifyCallback = new AuthifyCallback();

		try {
			if (this.debug) {
				log.info("callbackAuthifyTicket: before handle");
			}
			callbackHandler.handle(new Callback[] { authifyCallback });
			if (this.debug) {
				log.info("callbackAuthifyTicket: after handle");
			}
		} catch (IOException e) {
			LoginException ex = new LoginException(
					"IO error getting credentials: " + e.getMessage());
			e.initCause(e);
			throw ex;
		} catch (UnsupportedCallbackException e) {
			LoginException ex = new LoginException("UnsupportedCallback: "
					+ e.getMessage());
			e.initCause(e);
			throw ex;
		}
		result = authifyCallback.getAuthifyResponseToken();

		log.info("BM -----> login authifyResponseToken = [" + result + "]");
		return result;
	}

	private String retrieveUserDataToSharedState(String authifyResponseToken)
			throws LoginException {
		String bonitaUserName = null;

		if (authifyResponseToken == null
				|| authifyResponseToken.trim().length() == 0) {

			/*
			 * No Authify ticket provided
			 */

			String msg = "No or empty authifyResponseToken is provided with AuthifyCallback to AuthifyLoginModule JAAS module";
			log.info(msg);
			System.out.println(msg);
		} else {

			/*
			 * Found an Authify ticket. Verify ticket and load user data to
			 * sharedState
			 */

			String jsonAuthifyDataStr = null; // /"{\"data\": [{\"item\" : \"gmail,telia,nordea,bankid,telia_sign,bankid_sign,nordea_sign\",\"state\" : \"logout\",\"idp\" : \"gmail\",\"uid\" : \"10272\",\"mapuid\" : \"10272\",\"luid\" : \"\",\"idpuid\" : \"bjorn.molin@codeca.se\",\"name\" : \"Björn Molin\",\"email\" : \"bjorn.molin@codeca.se\",\"extra\" : \"name:Björn Molin email:bjorn.molin@codeca.se openid_ext1_value_country:Not available openid_ext1_value_lang:Not available openid_claimed_id:https://www.google.com/accounts/o8/id?id=AItOawnok9YsosciIqbemaSV02PAtGkE2N1ghBI \"}]}";

			try {
				if ("tolvan".equals(authifyResponseToken)) {
					jsonAuthifyDataStr = "{\"data\": [{\"item\" : \"gmail,telia,nordea,bankid,telia_sign,bankid_sign,nordea_sign\",\"state\" : \"logout\",\"idp\" : \"gmail\",\"uid\" : \"10272\",\"mapuid\" : \"10272\",\"luid\" : \"\",\"idpuid\" : \"1212121212\",\"name\" : \"Björn Molin\",\"email\" : \"bjorn.molin@codeca.se\",\"extra\" : \"name:Björn Molin email:bjorn.molin@codeca.se openid_ext1_value_country:Not available openid_ext1_value_lang:Not available openid_claimed_id:https://www.google.com/accounts/o8/id?id= \"}]}";
				} else {
					jsonAuthifyDataStr = AuthifyClientRest.get_response("json",
							authifyResponseToken);
				}

				System.out.println("jsonAuthifyDataStr=[" + jsonAuthifyDataStr
						+ "]");

				JSONObject authData = new JSONObject(jsonAuthifyDataStr);

				JSONArray eidUserArray = authData.getJSONArray("data");
				int len = eidUserArray.length();

				for (int i = 0; i < len; i++) {
					JSONObject userData = eidUserArray.getJSONObject(0);
					String idp = userData.getString("idp");
					String uid = userData.getString("uid");
					String idpuid = userData.getString("idpuid");
					String name = userData.getString("name");
					String email = userData.getString("email");
					String state = userData.getString("state");
					String mapuid = userData.getString("mapuid");

					log.severe("idp=" + idp);
					log.severe("uid=" + uid);
					log.severe("idpuid=" + idpuid);
					log.severe("name=" + name);
					log.severe("email=" + email);
					log.severe("state=" + state);
					log.severe("mapuid=" + mapuid);
					System.out.println("======> idp: " + idp);

					// TODO: plocka bara från idp bankid m.fl. och det räcker
					// med att få ett personnr till bonitaUserName
					// bankid har idpuid = personnr
					// verifiera att nordea och telia gör likadant....
					bonitaUserName = idpuid;
				}

			} catch (JSONException jsone) {
				log.severe("Failed to parse authify JSON data: "
						+ jsonAuthifyDataStr + " error message: "
						+ jsone.getMessage());
				LoginException le = new LoginException();
				le.initCause(jsone);
				throw le;
			} catch (Exception e) {
				log.severe("Exception during login while retrieving authify data: "
						+ e.toString());
				LoginException le = new LoginException();
				le.initCause(e);
				throw le;
			}

		}

		return bonitaUserName;
	}
}
