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
package org.motrice.doccheck;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Computes the check sums of one or more PDF documents.
 */
public class Main {
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static void main(String[] args) {
	Options opts = new Options(args);
	I18n i18n = new I18n(opts.getProperty(Options.LANGUAGE));

	for (String arg : args) {
	    if (arg.startsWith("-")) continue;
	    File file = new File(arg);
	    if (!(file.exists() && file.canRead())) {
		i18n.p("file.not.found", arg);
	    } else {
		process(arg, file, i18n, opts);
	    }
	}
    }

    private static void process(String fileName, File pdfFile, I18n i18n, Options opts) {
	try {
	    Processor processor = new Processor(fileName, copy(pdfFile), i18n, opts);
	    processor.process();
	} catch (IOException exc) {
	    i18n.p("hit.by.exception", exc.toString());
	} catch (AppException exc) {
	    i18n.p(exc.getMessage(), exc.toString());
	}
    }

    /**
     * Copy a file to a byte array
     */
    private static byte[] copy(File srcFile) throws IOException {
	FileInputStream input = new FileInputStream(srcFile);
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
	int n = 0;
	while (-1 != (n = input.read(buffer))) {
	    output.write(buffer, 0, n);
	}

	return output.toByteArray();
    }

}
