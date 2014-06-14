# Motrice Process Management #

This chapter defines management of BPMN processes by Coordinatrice and [other modules]. A *process* in this chapter refers to a BPMN process definition and its forms. Starting a process generates a running *process instance*. This text assumes Activiti is the process engine.

Status: This description is work in progress.

## Editing Processes ##

Processes may be edited through Coordinatrice, either interactively or over its REST interface. Editing a process may include editing its forms.

Process definitions in Activiti are in one of two states: *Active* or *Suspended*, The following description is taken from the Activiti JavaDoc, `RepositoryService.suspendProcessDefinitionById`.   Suspends the process definition with the given id. If a process definition is in state suspended, it will not be possible to start new process instances based on the process definition. Note: all the process instances of the process definition will still be active (ie. not suspended)!

Motrice uses a different set of process states to allow editing and testing. Activiti does not know about Motrice process states. The two sets of process states exist in parallel. The Motrice process states are defined as follows,


* **Edit**  
 The process and its forms are under construction, possibly in an inconsistent state. The process may be edited, but not started. The corresponding Activiti state is *Suspended*.
* **Trial**  
 The process and its forms are ready for test runs, but are not generally available. The process may be started, but only for testing by qualified users. Filled-out forms must be clearly marked as test data. *TODO: Define qualified users, define the test environment.* The corresponding Activiti state is *Active*.
* **Approved**  
 The process and its forms have been approved for general use but are not yet released. The process may be started, but only for testing by qualified users. Filled-out forms must be clearly marked as test data. The corresponding Activiti state is *Active*.
* **Published**  
 The process and its forms are released for general use. Filled-out forms are valid business input. The corresponding Activiti state is *Active*. A published process (or its forms) may not be modified, only retired.
* **Retired**  
 The process and its forms have been withdrawn from use. The corresponding Activiti state is *Suspended*. The process may not be started or modified, except that it may be re-published.

The *Approved* state is needed because release for general use sometimes needs to be scheduled for a future date.

Coordinatrice stores all process state definitions in the `crd_procdef_state` table.

## Starting Processes ##

Starting a process in Motrice is subject to conditions outside the Activiti engine. Coordinatrice maintains a table called `crd_procdef` parallel to the `act_re_procdef` table in the Activiti database. In both tables a row corresponds to a version of a process definition. A process definition ends up in the Coordinatrice table only if it has been edited. The logic for starting a Motrice process is,

1. Search for the process definition in `crd_procdef`.
2. If found, act according to the Motrice process state (see below). Get additional information from Activiti by using the primary key.
3. If not found, search for the process in Activiti. In this case an *Active* process is considered published and may be started.

## The crd_procdef Table ##

(Very preliminary.)


* May two users edit the same process independently? NO

The `crd_procdef` table is written by Coordinatrice. A row corresponds to a version of a process.

When a user starts editing a process the following happens,

1. A copy of the original process is deployed to Activiti. Its version number is incremented. The *Suspended* state is assigned to the copy.
2. A record is inserted into the `crd_procdef` table with the following initial properties.


* **actid (char)**  
 Activiti process id. Identical to column `id_`, primary key of the `act_re_procdef` table. Immutable.
* **actver (int)**  
 Process definition version, copied from `act_re_procdef.version_` . Immutable.
* **actdepl (char)**  
 Deployment id. Immutable.
* **procdef_state_id (int)**  
 Process state expressed as the id of a process state in the `crd_procdef_state` table. The ids are permanently assigned and may be used without referring to the database. This field changes with the editing process.

As soon as a process definition has been referred to by Coordinatrice there is a record in both tables (`act_re_procdef`, `crd_procdef`) with the same process id.

## The crd_procdef_state Table ##

Process definition states. A few columns contain business rules for the sake of other modules than Coordinatrice.

* **activiti_state**  
 The required state of this process in the Activiti engine.
* **editable**  
 Can this process definition be modified, either in form connections or the BPMN logic?
* **startable**  
 May this process definition spawn instances? The following values are defined: (1) Not startable; (2) Start in test mode only; (3) Start in production mode

Remaining columns:

* **default_message**  
 Default name of this state
* **res**  
 Resource name of this state (for localization)

