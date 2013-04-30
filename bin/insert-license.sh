#!/bin/sh

for i in $*
do
    if [[ $1 =~  .*java$ ]]
    then 
  # insert java style comment license header
	echo "Processing java file: " $1
	sed -i "1i\\
/* \\
 *  Process Aware Web Application Platform \\
 * \\
 *  Copyright (C) 2011-2013 Inherit S AB \\
 * \\
 *  This program is free software: you can redistribute it and/or modify \\
 *  it under the terms of the GNU Affero General Public License as published by \\
 *  the Free Software Foundation, either version 3 of the License, or \\
 *  (at your option) any later version. \\
 * \\
 *  This program is distributed in the hope that it will be useful, \\
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of \\
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the \\
 *  GNU Affero General Public License for more details. \\
 * \\
 *  You should have received a copy of the GNU Affero General Public License \\
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>. \\
 * \\
 *  e-mail: info _at_ inherit.se \\
 *  mail: Inherit S AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN \\
 *  phone: +46 8 641 64 14 \\
 */ \\
\\ " $1
    elif [[ $1 =~  .*jsp$ ]]
    then
  # insert java style comment license header
	echo "Processing jsp file: " $1
	sed -i "1i\\
<%-- \\
    Process Aware Web Application Platform \\
 \\
    Copyright (C) 2011-2013 Inherit S AB \\
 \\
    This program is free software: you can redistribute it and/or modify \\
    it under the terms of the GNU Affero General Public License as published by \\
    the Free Software Foundation, either version 3 of the License, or \\
    (at your option) any later version. \\
 \\
    This program is distributed in the hope that it will be useful, \\
    but WITHOUT ANY WARRANTY; without even the implied warranty of \\
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the \\
    GNU Affero General Public License for more details. \\
 \\
    You should have received a copy of the GNU Affero General Public License \\
    along with this program.  If not, see <http://www.gnu.org/licenses/>. \\
 \\
    e-mail: info _at_ inherit.se \\
    mail: Inherit S AB, Långsjövägen 8, SE-131 33 NACKA, SWEDEN \\
    phone: +46 8 641 64 14 \\
 --%> \\
\\ " $1
    else
  # unknown file extension, skipping
	echo "Unknown extension, file: " $1 
    fi
    shift
done
