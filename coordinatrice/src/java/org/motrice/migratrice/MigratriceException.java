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
package org.motrice.migratrice;

/**
 * Application-specific exception.
 * NOTE: When throwing a RuntimeException (also known as an "unchecked exception")
 * in a service, Grails (actually Spring) rolls back the transaction.
 * The controller must catch the exception.
 * In contrast, if a service throws a checked exception (non-RuntimeException)
 * the controller cannot catch it, and the transaction is not rolled back.
 */
public class MigratriceException extends RuntimeException {
    // 409 is "Conflict"
    public static final int DEFAULT_HTTP_RESPONSE_CODE = 409;
    // Message code
    protected String code;
    public String getCode() {return code;}
    // HTTP response code
    protected int http;
    public int getHttp() {return http;}
    public void setHttp(int http) {this.http = http;}

    public MigratriceException(String code, String message) {
	super(message);
	this.code = code;
	http = DEFAULT_HTTP_RESPONSE_CODE;
    }

    public MigratriceException(String code, String message, Throwable cause) {
	super(message, cause);
	this.code = code;
	http = DEFAULT_HTTP_RESPONSE_CODE;
    }

}
