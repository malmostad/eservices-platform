# Working Relative to a Release Branch #

 NOTE: This workflow is **deprecated** as of 2014-02-14. The text may still contain useful bits.

This workflow assumes you create a topic branch off a release branch named `activiti-engine`.

## Start-Out Basics ##

You may want to get the release branch up to date before starting.

```
git pull origin
```

[Solving the 'Your branch is ahead of...'](http://www.freewayprojects.com/2011/11/solving-the-your-branch-is-ahead-of-originmaster-by-x-commits-issue-after-pulling-using-git/).
If you are tempted to do `git pull origin activiti-engine` read

Then create the topic branch symbolically named BRANCH from the release branch.

```
git checkout -b BRANCH activiti-engine
```

Commit routinely as usual.

## Releasing A Branch ##
At some point you are ready to ship your tested changes back to the main repo, called *origin*.


* Make sure you are in the topic branch you want to release.

```
git checkout BRANCH
```


* Make sure there are no uncommitted changes in the branch.

```
git status
```

Load all changes that have happened in the central repo since you began the branch.

```
git fetch origin activiti-engine
```

Check if the changes can be merged.

```
git log --no-merges origin/activiti-engine ^BRANCH
```
The !^BRANCH notation means the head of the branch.

If there are no conflicts you are ready to merge changes into the local release branch. There are two sets of merges, origin/activiti-engine and the local BRANCH. The order is not important, but in this example we first merge origin/activiti-engine.

* Get back to the release branch

```
git checkout activiti-engine
```

* Get up to date by merging changes done by other developers

```
git merge origin/activiti-engine
```

* Merge my own topic branch

```
git merge BRANCH
```

Merging is not necessarily successful. There may be conflicting changes that must be resolved by a human (you). See the book, chapters 3.2.2 *Basic Merging* and 3.2.3 *Basic Merge Conflicts*.

If there are no problems, the final step is to push everything back to the central repo.

```
git push origin activiti-engine
```

Now your changes have been released and are part of the project's release branch.

## Deleting a Branch ##
Releasing a branch as described above means that the branch is first merged into the local release branch and then shipped to the central repo. At this point the branch may be deleted because there little further use of the branch.

A useful sanity check is,

```
git branch --merged
```
Branches without a "*" in front of them are generally fine to delete.

The command to delete a branch is,

```
git branch -d BRANCH
```

