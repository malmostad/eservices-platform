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
package org.motrice.jmx;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.management.Notification;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;

/**
 * Class for controlling log levels and receiving server error notifications
 * over JMX.
 */
@ManagedResource(objectName="coordinatrice:name=config,type=basicMgmt",
		 description="Basic application management over JMX",
		 log=true, logFile="basicjmxmanagement.log",
		 currencyTimeLimit=10)
public class BasicAppManagement implements NotificationPublisherAware {
    // Notification sequence number, incrementing should be thread safe
    private static Long SEQNO = 1L;
    // Notification publisher
    private NotificationPublisher publisher;

    /**
     * Get the names and log levels of all loggers.
     */
    @ManagedAttribute(description="Application loggers and log levels",
		      currencyTimeLimit=10)
    public List<String> getLoggers() {
	List<String> nameList = new ArrayList<String>();
	SortedMap<String,Logger> map = doGetLoggers();
	for (String key : map.keySet()) {
	    Logger log = map.get(key);
	    if (log != null && log.getLevel() != null) {
		nameList.add(key + " = " + log.getLevel());
	    }
	}

        return nameList;
    }

    /**
     * Get the log level of a named logger.
     * The first logger ending with the name is chosen.
     */
    @ManagedOperation(description="Get the log level of a specific logger",
		      currencyTimeLimit=10)
      @ManagedOperationParameters({
	      @ManagedOperationParameter(name="logger",
					 description="(Last part of) logger name")})
    public String getLogLevel(String logger) {
	// Note that getLogger returns a new logger if the name is undefined
	SortedMap<String,Logger> map = doGetLoggers();
	String level = "*Unknown*";
	if (notBlank(logger)) {
	    for (String key : map.keySet()) {
		if (key.endsWith(logger.trim())) {
		    Logger log = map.get(key);
		    if (log != null) level = log.getLevel().toString();
		}
	    }
	}

	return level;
    }

    /**
     * Set the level of a named logger.
     * All loggers ending with the name are chosen.
     */
    @ManagedOperation(description="Set the log level of a specific logger",
		      currencyTimeLimit=10)
      @ManagedOperationParameters({
	      @ManagedOperationParameter(name="logger",
					 description="(Last part of) logger name"),
	      @ManagedOperationParameter(name="level",
					 description="The new Log4j log level")})
    public void setLogLevel(String logger, String level) {
	// Note that getLogger returns a new logger if the name is undefined
	SortedMap<String,Logger> map = doGetLoggers();
	// The level is set to DEBUG if it does not match any known level
	Level newLevel = Level.toLevel(level.toUpperCase());
	if (notBlank(logger)) {
	    for (String key : map.keySet()) {
		if (key.endsWith(logger.trim())) {
		    Logger log = map.get(key);
		    if (log != null) log.setLevel(newLevel);
		}
	    }
	}
    }

    /**
     * Return all loggers as a map where the key is the full logger name,
     * the value is a logger.
     */
    private SortedMap<String,Logger> doGetLoggers() {
	TreeMap<String,Logger> map = new TreeMap<String,Logger>();
        for (Enumeration e = LogManager.getCurrentLoggers(); e.hasMoreElements(); ) {
            Logger log = (Logger) e.nextElement();
            if (log.getLevel() != null) {
		map.put(log.getName(), log);
            }
        }

	return map;
    }

    private boolean notBlank(String str) {
	return str != null && str.trim().length() > 0;
    }

    public void serverErrorNotification(Object userData, String requestUri,
					String message)
    {
	Notification no = new Notification("HTTP500", requestUri, SEQNO++, message);
	no.setUserData(userData);
	if (publisher != null) publisher.sendNotification(no);
    }

    //----------------- NotificationPublisherAware -----------------

    public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
        this.publisher = notificationPublisher;
    }

}
