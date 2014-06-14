# Changes in motrice forms #

## Database ##

The formPath is replaced with a double key (formTypeId,formDefinitionKey).


```
CREATE TABLE mtf_activity_form_definition
(
  activityformdefinitionid bigint NOT NULL,
  processdefinitionuuid character varying(255),
  activitydefinitionuuid character varying(255),
  formTypeId		bigint NOT NULL, -- FKEY to 
  formDefinitionKey 	character varying(255), -- Identfies a form. The formHandlerClass decides how to use it. Former formPath
  


CREATE TABLE mtf_form_type
(
formTypeId		bigint NOT NULL,	Identifies a form engine in motrice and a form handler bean. i.e. Orbeon, Sign form, Pay form, 
formTypeLabel           character varying(255), -- Form engine label to view in coordinatrice
formHandlerBean 	character varying(255), -- Spring bean name that identifies a MotriceFormHandler

```

TODO: ifall det lagras formulär instans data i mtf_tabeller så: formInstanceId        character varying(255) -- Identifies form instance data. The formHandlerClass decides how to use it. Former docId/formDocId. UUID is recommended. Annars föreställer jag mig att det kan vara i activitimotorn i alla fall för tasks.

## Process instance lifecycle ##
The following process variables is mandatory to set on submit of a start form. 


| Process variable | Type | Description |
|------------------|------|-------------|
|motrice.start.form.typeId| | |
|motrice.start.form.definitionKey| | |	
|motrice.start.form.instanceId | String | Unik identifierare som används i formulärhanteraren, helst uuid |
|motrice.start.form.dataUri  | String | Where to find data, typeId in combination with definitionKey should be enough to know the format of data |
|motrice.start.form.assignee |String |UserId, detta kan skilja sig från starter userId ifall processen startas av en å en annans vägnar (t.ex. helpdesk) |

## Motrice form lifecycle ## 

### First time load of task form ###
The following variables is mandatory to set on the first time load (rendering) of a task form. 

| Local task variable | Type | Description |
|---------------------|------|-------------|
|motrice.form.typeId    | int    | From matching (processdefinitionuuid and activitydefinitionuuid) mtf_activity_form_definition.formTypeId |
|motrice.form.definitionKey	   | String    |  From matching (processdefinitionuuid and activitydefinitionuuid) mtf_activity_form_definition.formDefinitionKey |
|motrice.form.instanceId  | String | Unik identifierare som används i formulärhanteraren, helst uuid |
|motrice.form.dataUri  | String | Where to find data, typeId in combination with definitionKey should be enough to know the format of data |

Further, there is a taskId in the activiti engine that the form handler should pass to the form engine.

### Subsequent loads of initialized task form ###
Use the Local task variables in order to render the form. After the task form is submitted, the edit url should be null. 

### Submit of task form ###
After task form submit, the task in the activiti engine is reported as executed.

## Execution listeners ##
### Create an act for digital preservation ###

#### CreateDocBoxActExecutionListener on task ####

Run this execution listener on task's end event in order to create a pdf/a act of the task form in DocBox for digital preservation.

How to add execution listener to task:

```
<userTask id#"Registrering" name#"Registrering">
   <extensionElements>
      <activiti:executionListener 
          event="end" 
          class="org.inheritsource.service.delegates.CreateDocBoxActExecutionListener">     
      </activiti:executionListener>
   </extensionElements>
</userTask>
```

Local task variable motrice.form.preservation.docBoxRef is assigned. 

#### CreateDocBoxActExecutionListener on start form ####

Run this execution listener on process's start event in order to create a pdf/a act of the start form in DocBox for digital preservation.

How to add execution listener to start form (i.e. process instance start event):

```
<startEvent id#"startevent1" name#"Start">
   <extensionElements>
      <activiti:executionListener 
          event="start" 
          class="org.inheritsource.service.delegates.CreateDocBoxActExecutionListener">
      </activiti:executionListener>
   </extensionElements>
</startEvent>
```

Process variable motrice.start.form.preservation.docBoxRef is assigned. 

## Form Handlers ##
TODO Definiera vad form handler gör... interface org.inheritsource.taskform.form.FormHandler

### Orbeon ###
Orbeon form 

|formHandlerBean|motriceOrbeonFormHandler|set formTypeId corresponding to this handler bean|
|---------------|------------------------|-------------------------------------------------|
|formDefinitionKey|formPath|Note that in motrice this form path includes form version|

### SignStartForm ###
Sign the process instance start form.

|formHandlerBean|motriceSignStartFormHandler|set formTypeId corresponding to this handler bean|
|---------------|---------------------------|-------------------------------------------------|
|formDefinitionKey|n/a||

The process instance variable motrice.start.form.preservation.docBoxRef has to be set before this task. This is done by adding CreateDocBoxActExecutionListener to the process start event in process diagram.

The task's local variable motrice.form.preservation.docBoxRef will be set to the signed document after submission.

Fundera på: Kanske inte ställa krav på färdig docBoxRef??Det går ju att producera on the fly med.

### SignTask ###
Sign the form behind a previously executed task.

|formHandlerBean|motriceSignTaskFormHandler|set formTypeId corresponding to this handler bean|
|---------------|--------------------------|-------------------------------------------------|
|formDefinitionKey|taskDefinitionKey|Specify a previously executed task in process instance to sign. The task definition key is the id of the userTask: <userTask id="xxx" .../> |

Task with id taskDefinitionKey should by convention have the following local variables: motrice.form.preservation.docBoxRef. This is done by adding CreateDocBoxActExecutionListener to the end event of the task with id taskDefinitionKey.

The task's local variable motrice.form.preservation.docBoxRef will be set to the signed document after submission.

Fundera på: Kanske inte ställa krav på färdig docBoxRef??Det går ju att producera on the fly med.

### PayTask ###
Payment form 

|formHandlerBean|motricePaymentFormHandler|set formTypeId corresponding to this handler bean|
|---------------|-------------------------|-------------------------------------------------|
|formDefinitionKey|taskDefinitionKey|Specify a previously executed task in process instance to pay. The task definition key is the id of the userTask: <userTask id="xxx" .../> |

Task with id taskDefinitionKey should by convention have the following local variables:

TODO: specify motrice.payment.vat, motrice.payment.amount  etc


### Notify form ###
View a digitally preserved act and log the view and/or end user confirmation of read

|formHandlerBean|motriceNotifyActFormHandler|set formTypeId corresponding to this handler bean|
|---------------|---------------------------|-------------------------------------------------|
|formDefinitionKey|taskDefinitionKey|Specify a previously executed task in process instance to notify. The task definition key is the id of the userTask: <userTask id="xxx" .../> |

Task with id taskDefinitionKey should by convention have the following local variables:

motrice.form.preservation.docBoxRef. This is done by adding CreateDocBoxActExecutionListener to the end event of the task with id taskDefinitionKey.

### No form ###
This form will view a form with only a submit button. The end user is expected to read the guide and perform some work and click submit when finished.


|formHandlerBean|motriceNoFormHandler|set formTypeId corresponding to this handler bean|
|---------------|--------------------|-------------------------------------------------|
|formDefinitionKey|n/a| |


