# Git bra att ha #


* Ta reda på commits vid en viss tidpunkt (som ligger utanför den vanliga refloggen).

```
git rev-list --max-count#1 --before#"2012-02-03 17:55" master
```
Svaret kan användas för en checkout.


* Ta reda på taggar i närheten av en commit.

```
git describe --candidates 10 cbeac54c54b786e99d11d542299eb5998fc5278e
```

