package com.dafttech.stream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ByteArrayInputStreamOutputStream extends OutputStream {
    protected ByteArrayInputStreamAccess inputStreamAccess;

    private boolean markEnabled = false;

    public ByteArrayInputStreamOutputStream(ByteArrayInputStream inputStream) {
        inputStreamAccess = new ByteArrayInputStreamAccess(inputStream);
    }

    public void setMarkEnabled(boolean value) {
        markEnabled = value;
    }

    /**
     * Increases the capacity if necessary to ensure that it can hold at least
     * the number of elements specified by the minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity
     * @throws OutOfMemoryError if {@code minCapacity < 0}. This is interpreted as a request
     *                          for the unsatisfiably large capacity
     *                          {@code (long) Integer.MAX_VALUE + (minCapacity - Integer.MAX_VALUE)}
     *                          .
     */
    private void ensureCapacity(int minCapacity) {
        // overflow-conscious code
        if (!markEnabled) inputStreamAccess.inputStream.mark(0);
        if (minCapacity - inputStreamAccess.getBuf().length > 0) grow(minCapacity);
        clean();
    }

    /**
     * Increases the capacity to ensure that it can hold at least the number of
     * elements specified by the minimum capacity argument.
     *
     * @param minCapacity the desired minimum capacity
     */
    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = inputStreamAccess.getBuf().length;
        int newCapacity = oldCapacity << 1;
        if (newCapacity - minCapacity < 0) newCapacity = minCapacity;
        if (newCapacity < 0) {
            if (minCapacity < 0) // overflow
                throw new OutOfMemoryError();
            newCapacity = Integer.MAX_VALUE;
        }
        inputStreamAccess.setBuf(Arrays.copyOf(inputStreamAccess.getBuf(), newCapacity));
    }

    private void clean() {
        int pos = inputStreamAccess.getPos(), mark = inputStreamAccess.getMark(), count = inputStreamAccess.getCount();
        if (pos > 0 && mark > 0 && count > 0) {
            int amount = Math.min(Math.min(pos, mark), count);
            byte[] buf = inputStreamAccess.getBuf();
            inputStreamAccess.setBuf(Arrays.copyOfRange(buf, amount, buf.length));
            inputStreamAccess.setPos(pos - amount);
            inputStreamAccess.setMark(mark - amount);
            inputStreamAccess.setCount(count - amount);
        }
    }

    /**
     * Writes the specified byte to this byte array output stream.
     *
     * @param b the byte to be written.
     */
    @Override
    public synchronized void write(int b) {
        int count = inputStreamAccess.getCount();
        ensureCapacity(count + 1);
        count = inputStreamAccess.getCount();
        inputStreamAccess.getBuf()[count] = (byte) b;
        inputStreamAccess.setCount(count + 1);
    }

    /**
     * Writes <code>len</code> bytes from the specified byte array starting at
     * offset <code>off</code> to this byte array output stream.
     *
     * @param b   the data.
     * @param off the start offset in the data.
     * @param len the number of bytes to write.
     */
    @Override
    public synchronized void write(byte b[], int off, int len) {
        if (off < 0 || off > b.length || len < 0 || off + len - b.length > 0) {
            throw new IndexOutOfBoundsException();
        }
        int count = inputStreamAccess.getCount();
        ensureCapacity(count + len);
        count = inputStreamAccess.getCount();
        System.arraycopy(b, off, inputStreamAccess.getBuf(), count, len);
        inputStreamAccess.setCount(count + len);
    }

    /**
     * Writes the complete contents of this byte array output stream to the
     * specified output stream argument, as if by calling the output stream's
     * write method using <code>out.write(buf, 0, count)</code>.
     *
     * @param out the output stream to which to write the data.
     * @throws IOException if an I/O error occurs.
     */
    public synchronized void writeTo(OutputStream out) throws IOException {
        out.write(inputStreamAccess.getBuf(), 0, inputStreamAccess.getCount());
    }

    public void mark(int readAheadLimit) {
        inputStreamAccess.inputStream.mark(readAheadLimit);
    }

    /**
     * Resets the <code>count</code> field of this byte array output stream to
     * zero, so that all currently accumulated output in the output stream is
     * discarded. The output stream can be used again, reusing the already
     * allocated buffer space.
     *
     * @see java.io.ByteArrayInputStream#count
     */
    public synchronized void reset() {
        inputStreamAccess.inputStream.reset();
    }

    /**
     * Creates a newly allocated byte array. Its size is the current size of
     * this output stream and the valid contents of the buffer have been copied
     * into it.
     *
     * @return the current contents of this output stream, as a byte array.
     * @see java.io.ByteArrayOutputStream#size()
     */
    public synchronized byte toByteArray()[] {
        return Arrays.copyOf(inputStreamAccess.getBuf(), inputStreamAccess.getCount());
    }

    /**
     * Returns the current size of the buffer.
     *
     * @return the value of the <code>count</code> field, which is the number of
     * valid bytes in this output stream.
     * @see java.io.ByteArrayOutputStream#count
     */
    public synchronized int size() {
        return inputStreamAccess.getCount();
    }

    /**
     * Converts the buffer's contents into a string decoding bytes using the
     * platform's default character set. The length of the new <tt>String</tt>
     * is a function of the character set, and hence may not be equal to the
     * size of the buffer.
     * <p/>
     * <p/>
     * This method always replaces malformed-input and unmappable-character
     * sequences with the default replacement string for the platform's default
     * character set. The {@linkplain java.nio.charset.CharsetDecoder} class
     * should be used when more control over the decoding process is required.
     *
     * @return String decoded from the buffer's contents.
     * @since JDK1.1
     */
    @Override
    public synchronized String toString() {
        return new String(inputStreamAccess.getBuf(), 0, inputStreamAccess.getCount());
    }

    /**
     * Converts the buffer's contents into a string by decoding the bytes using
     * the named {@link java.nio.charset.Charset charset}. The length of the new
     * <tt>String</tt> is a function of the charset, and hence may not be equal
     * to the length of the byte array.
     * <p/>
     * <p/>
     * This method always replaces malformed-input and unmappable-character
     * sequences with this charset's default replacement string. The
     * {@link java.nio.charset.CharsetDecoder} class should be used when more
     * control over the decoding process is required.
     *
     * @param charsetName the name of a supported {@link java.nio.charset.Charset
     *                    charset}
     * @return String decoded from the buffer's contents.
     * @throws UnsupportedEncodingException If the named charset is not supported
     * @since JDK1.1
     */
    public synchronized String toString(String charsetName) throws UnsupportedEncodingException {
        return new String(inputStreamAccess.getBuf(), 0, inputStreamAccess.getCount(), charsetName);
    }

    /**
     * Closing a <tt>ByteArrayOutputStream</tt> has no effect. The methods in
     * this class can be called after the stream has been closed without
     * generating an <tt>IOException</tt>.
     */
    @Override
    public void close() throws IOException {
    }
}
