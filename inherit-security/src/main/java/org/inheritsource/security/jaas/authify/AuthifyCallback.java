package org.inheritsource.security.jaas.authify;

import javax.security.auth.callback.Callback;

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

public class AuthifyCallback implements Callback {

	private String authifyResponseToken;

	public AuthifyCallback() {

	}

	public String getAuthifyResponseToken() {
		return authifyResponseToken;
	}

	public void setAuthifyResponseToken(String authifyResponseToken) {
		this.authifyResponseToken = authifyResponseToken;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((authifyResponseToken == null) ? 0 : authifyResponseToken
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthifyCallback other = (AuthifyCallback) obj;
		if (authifyResponseToken == null) {
			if (other.authifyResponseToken != null)
				return false;
		} else if (!authifyResponseToken.equals(other.authifyResponseToken))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AuthifyCallback: authifyResponseToken='" + authifyResponseToken + "'";
	}

}
