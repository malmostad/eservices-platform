
## Activiti ##


### executionId and processInstanceId ###

A process instance is also an execution. The process instance id = the root execution id of the tree of all executions. Ie, when the process splits into two parallel paths, there will be two executions under the root (ie process instance)


super_exec: stored relationship between the process and subprocess when using call activity

parent id: is used in various places, eg when doing concurrency etc. You should be able to find your way up to the process instance id by following both.


ProcessInstance extends Execution: Represents one execution of a ProcessDfinition.

Execution: getId() Unique identifier            getProcessInstanceId() Id of the root of the execution tree representing the process instance. Same as getId() if this execution is the process instance.






## Gammalt om Postgres ##
Skapa APT för Postgresql 9.2 så får man skapa filen /etc/apt/sources.list.d/pgdg.list med innehållet deb http://apt.postgresql.org/pub/repos/apt/ precise-pgdg main

Sedan lägger man till en repository key för att det skall fungera: wget --quiet -O - http://apt.postgresql.org/pub/repos/apt/ACCC4CF8.asc | sudo apt-key add - [[BR]] sudo apt-get update

Sedan kör man installationen: sudo apt-get install postgresql pgadmin3

$ sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'postgres';"
Vid nyinstallation av Postgresql var det bra att börja med:

















