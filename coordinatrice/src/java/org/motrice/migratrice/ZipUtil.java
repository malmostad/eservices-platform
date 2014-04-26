/* == Motrice Copyright Notice ==
 *
 * Motrice Service Platform
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
package org.motrice.migratrice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipInputStream;

/**
 * Utility for reading data from a zip input stream
 */
public class ZipUtil {
    // Buffer length
    protected static final int BUFLEN = 10240;

    /**
     * Don't construct
     */
    private ZipUtil() {}

    /**
     * Read data from the stream.
     * The point is not to close it afterwards.
     */
    public static ByteArrayOutputStream read(ZipInputStream zis) throws IOException {
	byte[] buf = new byte[BUFLEN];
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	int len = 0;
	while ((len = zis.read(buf)) >= 0) {
	    baos.write(buf, 0, len);
	}

	return baos;
    }

}
