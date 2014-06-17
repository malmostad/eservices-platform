# Git Workflow for Motrice #

Detta flöde gäller från 2014-02-14 (Alla Hjärtans Dag). Passar bättre för ett större projekt än det tidigare "single-page".

Flödet beskrivs här: [A successful Git branching model](http://nvie.com/posts/a-successful-git-branching-model/)

## Första gången ##

Björn har skapat en ny branch `develop` från den tidigare `activiti-engine`.

Första gången man checkar ut remote branchen skall man köra

```
git fetch origin
git checkout -b develop  origin/develop
```
Det kopplar ihop lokal develop branch med remote sådan.

Nästa gång man switchar till den branchen kör man bara `git checkout develop`

Björn fick göra 

```
git branch --set-upstream-to=origin/develop develop
```
## Översikt ##
men det kan bero på att Björn inte hade följt instruktionen. Ifall får göra detsamma så måste intruktionen uppdateras.

![Untitled image](git-branching-model.png)

