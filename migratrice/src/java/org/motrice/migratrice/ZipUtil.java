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
