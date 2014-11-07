package org.inheritsource.service.docbox;

import org.inheritsource.service.processengine.ActivitiEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PollSignatureScheduler {
	public static final Logger log = LoggerFactory.getLogger(PollSignatureScheduler.class.getName());
	
	ActivitiEngineService activitiEngineService;
	
	public PollSignatureScheduler() {
		
	}

	public ActivitiEngineService getActivitiEngineService() {
		return activitiEngineService;
	}

	public void setActivitiEngineService(ActivitiEngineService activitiEngineService) {
		this.activitiEngineService = activitiEngineService;
	}

	public void run() {
	    try { 
	    	log.debug("Start PollSignatureScheduler run");
	    	activitiEngineService.pollCompletedSignRequest();
	    } catch (Exception e) {
	    	log.error("Failed in PollSignatureScheduler run: " + e.toString());
	    }
	 
	  }
}
