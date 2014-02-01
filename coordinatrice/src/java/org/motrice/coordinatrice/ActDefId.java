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
