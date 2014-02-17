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
