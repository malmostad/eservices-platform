# Source Code Headers #

Text to be inserted at the top of every source code file. The last year of the copyright statement must always be the current year.

## Java/Groovy ##

```
/*
 * Motrice service platform
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
```

## JSP/GSP ##

```
<%--
  Motrice service platform

  Copyright (C) 2011-2014 Motrice AB

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU Affero General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU Affero General Public License for more details.

  You should have received a copy of the GNU Affero General Public License
  along with this program. If not, see <http://www.gnu.org/licenses/>.

  e-mail: info _at_ motrice.se
  mail: Motrice AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN
  phone: +46 8 641 64 14
--%>
```

## Using *find*##

Sample command to find all `.java`, `.groovy` and `.gsp` files in a Grails project where the `plugins` directory should be excluded.

```
find . \( -name plugins -prune \) -o \( -name \*.groovy -o -name \*.java \) -print
```

## Using *editGrailsCopyright* ##

This is a Groovy script for inserting or replacing copyright notices in the source code of a Grails application. See the `bin/grailsCopyright` directory at the repository top.


