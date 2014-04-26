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

import java.io.ByteArrayOutputStream;

/**
 * Special-purpose PDF reader.
 * Assumes the entire PDF file can be kept in a byte array, implying a
 * maximum file size of 2 GB.
 * It is possible to set an artificial length.
 */
public class LimitedPdfReader {
    // End-of-file marker
    public static final int EOF = -1;

    // Maximum line length before giving up
    protected static final int MAX_LINE_LENGTH = 400;

    // The entire PDF document
    protected byte[] pdf;

    // Index of the next byte to be read from the document.
    protected int pos = 0;

    // Length of the document (in bytes).
    // Useful for processing document steps.
    protected int length;

    protected StringBuilder sb = new StringBuilder();

    public LimitedPdfReader(byte[] document) {
	pdf = document;
	pos = 0;
	length = document.length;
    }

    /**
     * Get the current byte offset in the document.
     */
    public int getPos() {
	return pos;
    }

    /**
     * Get a slice of the document.
     * @param startOffset must be the index of the first byte to return
     * @param endOffset must be the index following the last byte to return,
     * @return a byte array containing the bytes
     */
    public byte[] getSlice(int startOffset, int endOffset) {
	if (startOffset < 0 || startOffset > pdf.length)
	    throw new AppException("invalid.start.offset");
	if (endOffset < 0 || endOffset > pdf.length || endOffset <= startOffset)
	    throw new AppException("invalid.end.offset");
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	for (int idx = startOffset; idx < endOffset; idx++) {
	    baos.write(pdf[idx]);
	}

	return baos.toByteArray();
    }

    /**
     * Read a single byte.
     * @return the byte, or -1 if end of document.
     */
    public int read() {
	return (pos < length)? pdf[pos++] : EOF;
    }

    /**
     * Return the next byte to be read without reading it.
     * @return the byte, or -1 if end of document.
     */
    public int peek() {
	return (pos < length)? pdf[pos] : EOF;
    }

    /**
     * Remaining bytes to read.
     */
    public int maxRead() {
	return length - pos;
    }

    public boolean eof() {
	return maxRead() == 0;
    }

    /**
     * Read a chunk.
     * @return a byte array, either the original buffer filled with bytes
     * from the document, or, if there are not enough bytes to fill the buffer,
     * a newly allocated buffer, or null on end of file.
     */
    public byte[] read(byte[] buf) {
	if (eof()) return null;
	// Allocate a new buffer if we cannot fill the incoming
	byte[] bytes = (buf.length > maxRead())? new byte[maxRead()] : buf;
	// Just to be very clear
	int bytesToCopy = bytes.length;
	System.arraycopy(pdf, pos, bytes, 0, bytesToCopy);
	pos += bytesToCopy;
	return bytes;
    }

    /**
     * Read bytes up to and including the next end-of-line.
     * Convert the bytes to String.
     * @return the next line minus trailing end-of-line characters, or null
     * if there are no more lines.
     * If the line length exceeds the fixed limit, return an empty string.
     * Streams may form very long lines and are not interesting to this
     * application.
     */
    public String readLine() {
	if (eof()) return null;
	sb.setLength(0);
	int ch = 0;
	boolean eol = false;
	int count = 0;

	while (!eol) {
	    switch (ch = read()) {
	    case EOF:
	    case '\n':
		eol = true;
		break;
	    case '\r':
		eol = true;
		if (peek() == '\n') read();
		break;
	    default:
		if (count < MAX_LINE_LENGTH) {
		    sb.append((char)ch);
		} else if (sb.length() > 0) {
		    sb.setLength(0);
		}
		count++;
		break;
	    }
	}

	return sb.toString();
    }

    /**
     * Reset the reader for reuse.
     * No change to contents.
     */
    public void reset() {
	pos = 0;
	length = pdf.length;
    }

    /**
     * Reset to a specific length.
     */
    public void reset(int length) {
	pos = 0;
	this.length = length;
    }

}
