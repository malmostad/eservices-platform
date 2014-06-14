# Single-Page Git Workflow and Git Basics #

 NOTE: The workflow is **deprecated** as of 2014-02-14. The text may still contain useful Git basic stuff.

Suggested workflow. Taken from the free book [Git Pro](http://git-scm.com/book), chapter 5.2.2 *Private Small Team*. The workflow assumes there is a central repo with a small number of geographically distributed, cooperating developers. It also assumes the repo is not open to the general public.

The main idea is to keep master (the trunk) releasable at all times. Hacking is done in branches, one branch for each hack, and then merging back to master. The term *topic branch* is sometimes used to indicate that you create a branch for a defined purpose. It's perfectly possible to have several active branches going on at the same time.

The workflow point of view is the developer who also is a committer. In this setup every developer is *remote*.

If you find yourself in a fix, read [On undoing, fixing or removing commits in git](http://sethrobertson.github.io/GitFixUm/fixup.html).

## Original Clone ##
Everything starts from cloning the repo, done once.

```
git clone https://github.com/inheritsource/pawap.git
```
The command creates a local copy of the repo in your local box. The link back to the original is kept.

## Create a Branch: checkout -b BRANCH ##
For every fix, create a topic branch like this,

```
git checkout -b BRANCH
```
where BRANCH is a suitable branch name. An actual branch name conventionally uses mostly lower case. It could refer to a ticket or the name of a new feature. It is a topic branch because it has a well-defined purpose. Only a few patterns are **excluded** from branch names.

* A path component that begins with "."
* Double dot ".."
* End with a "/"
* End with ".lock"
* Contain a "\" (backslash)

It's possible to create a branch for the sole purpose of experimenting, an experimental branch. The main difference from a topic branch is that an experimental branch ends up being deleted rather than merged back to master.

In any case, the idea is that whenever you edit anything, you do it in a branch. Don't touch master, except by merging branches.

## List Branches ##
The simplest command to find out the current status from a Git point of view is,

```
git status
```

To list current branches with the current one being prefixed by "*",

```
git branch
```

To see the last commit on every branch,

```
git branch -v
```

To see what branches have been merged,

```
git branch --merged
```
Those without the "*" prefix are generally fine to delete. Use the `--no-merged` option to see branches that contain work you have not yet merged in.

## Committing: commit -a -m ##
You may commit your work in a branch at any time, no problem. You don't have to postpone committing because changes are incomplete or inconsistent The changes only affect the current branch. The current branch is only visible in your local repo. The oft-repeated command is,

```
git commit -a -m 'Commit message here'
```

## Switching Between Branches: checkout ##
You may switch between branches at any time, like this,

```
git checkout BRANCH
```
This includes switching back to master:

```
git checkout master
```
For instance, you may switch back to master in order to create a new, independent topic branch, no problem.

## Fixing Edits in Wrong Branch ##

Case in point: You edit a few files while in branch master only to realize that you intended to work in branch BRANCH. No need to panic. Use the following commands.

```
git stash
git checkout BRANCH
git stash apply
```
possibly followed by a commit in the new branch.

## Releasing A Branch ##
At some point you are ready to ship your tested changes back to the main repo, called *origin*. We assume you want to ship a topic branch named BRANCH.


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
git fetch origin
```

Check if the changes can be merged.

```
git log --no-merges origin/master ^BRANCH
```
The !^BRANCH notation means the head of the branch.

If there are no conflicts you are ready to merge changes into the local master. There are two sets of merges, origin/master and the local BRANCH. The order is not important, but in this example we first merge origin/master.

* Get back to master

```
git checkout master
```

* Get up to date by merging changes done by other developers

```
git merge origin/master
```

* Merge my own topic branch

```
git merge BRANCH
```

Merging is not necessarily successful. There may be conflicting changes that must be resolved by a human (you). See the book, chapters 3.2.2 *Basic Merging* and 3.2.3 *Basic Merge Conflicts*.

If there are no problems, the final step is to push everything back to the central repo.

```
git push origin master
```

Now your changes have been released and are part of the project's master.

## Deleting a Branch ##
Releasing a branch as described above means that the branch is first merged into the local master and then shipped to the central repo. At this point the branch may be deleted because there little further use of the branch.

A useful sanity check is,

```
git branch --merged
```
Branches without a "*" in front of them are generally fine to delete.

The command to delete a branch is,

```
git branch -d BRANCH
```

