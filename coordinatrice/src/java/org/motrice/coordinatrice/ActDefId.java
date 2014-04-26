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
package org.motrice.coordinatrice;

/**
 * The id of an activity definition.
 * It is the combination of a process and an activity id.
 * The activity id is relative to the process where it is defined.
 * NOTE: Using the full id as a string is deprecated but useful
 * for display.
 */
public class ActDefId {
    public static final char SEP = '@';

    // Process unique id
    private String procId;

    // Activity id within the process
    private String actId;

    /**
     * Construct from its parts
     */
    public ActDefId(String procId, String actId) {
	this.procId = procId;
	this.actId = actId;
    }

    /**
     * Construct from string format
     */
    public ActDefId(String id) {
	int idx = id.lastIndexOf(SEP);
	if (idx <= 0 || idx >= id.length())
	    throw new IllegalArgumentException("Invalid activity id '" + id + "'");
	procId = id.substring(0, idx);
	actId = id.substring(idx + 1);
    }

    public String getProcId() {return procId;}

    public String getActId() {return actId;}

    public String toString() {
	return procId + SEP + actId;
    }
}
