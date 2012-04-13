package org.inherit.taskform.engine.bonitautil;

import java.util.logging.Logger;

import org.inherit.taskform.engine.persistence.entity.ActivityDefinition;
import org.inherit.taskform.engine.persistence.entity.ProcessDefinition;
import org.ow2.bonita.facade.def.majorElement.ProcessDefinition.ProcessState;

public class BonitaObjectConverter {
	public static final Logger log = Logger.getLogger(BonitaObjectConverter.class.getName());
			
	public static void convert(org.ow2.bonita.facade.def.majorElement.ProcessDefinition boProcDef, ProcessDefinition procDef) {
		procDef.setUuid(boProcDef.getUUID().getValue());
		procDef.setLabel(boProcDef.getLabel());
		ProcessState state = boProcDef.getState();
		if (ProcessState.ENABLED.equals(state)) {
			procDef.setStatus(ProcessDefinition.STATUS_ENABLED);
		}
		else if (ProcessState.DISABLED.equals(state)) {
			procDef.setStatus(ProcessDefinition.STATUS_DISABLED);
		} 
		else if (ProcessState.ARCHIVED.equals(state)) {
			procDef.setStatus(ProcessDefinition.STATUS_ARCHIVED);
		}
		else {
			log.severe("Unknown process state in converter, state=[" + state + "]");
		}
		
	}
	
	public static void convert(org.ow2.bonita.facade.def.majorElement.ActivityDefinition boActDef, ActivityDefinition actDef) {
		actDef.setUuid(boActDef.getUUID().getValue());
		actDef.setLabel(boActDef.getLabel());
	}
}
