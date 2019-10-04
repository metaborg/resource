package mb.resource.text;

import java.io.*;


/**
 * Base class for a class that implements both {@link DataInput} and {@link DataOutput}.
 */
public abstract class DataInputOutput implements DataOutput, DataInput, Closeable {

    protected static final int EOF = -1;

    /**
     * Peeks a single byte.
     *
     * @return The peeked byte; or -1 when the end of the data was reached.
     * @throws IOException An I/O exception occurred.
     */
    public abstract int peek() throws IOException;

    /**
     * Reads a single byte.
     * @return The read byte; or -1 when the end of the data was reached.
     * @throws IOException An I/O exception occurred.
     */
    public abstract int read() throws IOException;

    /**
     * Reads bytes into a buffer.
     * @param buffer The buffer to read into.
     * @param offset The offset in the buffer to start writing to.
     * @param length The maximum number of bytes to read.
     * @return The number of bytes read; or -1 when the end of the stream was reached.
     * @throws IOException An I/O exception occurred.
     */
    public int read(byte[] buffer, int offset, int length) throws IOException {
        if (offset < 0 || length < 0 || offset + length > buffer.length)
            throw new IndexOutOfBoundsException();

        // You probably want to override this.
        for (int i = offset; i < offset + length; i++) {
            int b = read();
            if (b == EOF) return i - offset;
            buffer[i] = (byte)b;
        }
        return length;
    }

    /**
     * Writes a single byte.
     * @param b The byte to write.
     * @throws IOException An I/O exception occurred.
     */
    public abstract void write(int b) throws IOException;

    /**
     * Writes bytes from a buffer.
     * @param buffer The buffer to write from.
     * @param offset The opffset in the buffer to start reading from.
     * @param length The number of bytes to write.
     * @throws IOException An I/O exception occurred.
     */
    @Override
    public void write(byte[] buffer, int offset, int length) throws IOException {
        if (offset < 0 || length < 0 || offset + length > buffer.length)
            throw new IndexOutOfBoundsException();

        // You probably want to override this.
        for (int i = offset; i < offset + length; i++) {
            write(buffer[i]);
        }
    }

    @Override
    public abstract int skipBytes(int n) throws IOException;

    // Reading

    @Override
    public void readFully(byte[] byteArray) throws IOException {
        readFully(byteArray, 0, byteArray.length);
    }

    @Override
    public void readFully(byte[] byteArray, int offset, int length) throws IOException {
        int totalRead = 0;
        while (totalRead < length) {
            int read = read(byteArray, offset + totalRead, length - totalRead);
            if (read < 0) throw new EOFException();
            totalRead += read;
        }
    }

    @Override
    public boolean readBoolean() throws IOException {
        return readByte() != 0;
    }

    @Override
    public byte readByte() throws IOException {
        return (byte)readUnsignedByte();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        int b = this.read();
        if (b < 0) throw new EOFException();
        return b;
    }

    @Override
    public short readShort() throws IOException {
        return (short)readUnsignedShort();
    }

    @Override
    public int readUnsignedShort() throws IOException {
        int b0 = readUnsignedByte() << 8;
        int b1 = readUnsignedByte() << 0;
        return b0 | b1;
    }

    @Override
    public char readChar() throws IOException {
        return (char)readUnsignedShort();
    }

    @Override
    public int readInt() throws IOException {
        int b0 = readUnsignedByte() << 24;
        int b1 = readUnsignedByte() << 16;
        int b2 = readUnsignedByte() <<  8;
        int b3 = readUnsignedByte() <<  0;
        return b0 | b1 | b2 | b3;
    }

    @Override
    public long readLong() throws IOException {
        long b0 = ((long)readUnsignedByte()) << 56;
        long b1 = ((long)readUnsignedByte()) << 48;
        long b2 = ((long)readUnsignedByte()) << 40;
        long b3 = ((long)readUnsignedByte()) << 32;
        long b4 = ((long)readUnsignedByte()) << 24;
        long b5 = ((long)readUnsignedByte()) << 16;
        long b6 = ((long)readUnsignedByte()) <<  8;
        long b7 = ((long)readUnsignedByte()) <<  0;
        return b0 | b1 | b2 | b3 | b4 | b5 | b6 | b7;
    }

    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public String readLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean eol = false;
        do {
            int c = read();
            if (c == '\n' || c == '\r' || c == EOF) {
                eol = true;
                if (c == '\r' && peek() == '\n') {
                    // Consume the '\n'
                    read();
                }
            } else {
                sb.append((char)c);
            }
        } while (!eol);

        if (sb.length() == 0) return null;
        return sb.toString();
    }

    @Override
    public String readUTF() throws IOException {
        throw new UnsupportedOperationException();
    }


    // Writing

    @Override
    public void write(byte[] byteArray) throws IOException {
        write(byteArray, 0, byteArray.length);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        writeByte(v ? 1 : 0);
    }

    @Override
    public void writeByte(int v) throws IOException {
        write(v & 0xFF);
    }

    @Override
    public void writeShort(int v) throws IOException {
        writeByte(v >> 8);
        writeByte(v >> 0);
    }

    @Override
    public void writeChar(int v) throws IOException {
        writeShort(v);
    }

    @Override
    public void writeInt(int v) throws IOException {
        writeByte(v >> 24);
        writeByte(v >> 16);
        writeByte(v >>  8);
        writeByte(v >>  0);
    }

    @Override
    public void writeLong(long v) throws IOException {
        writeByte((int)(v >> 56));
        writeByte((int)(v >> 48));
        writeByte((int)(v >> 40));
        writeByte((int)(v >> 32));
        writeByte((int)(v >> 24));
        writeByte((int)(v >> 16));
        writeByte((int)(v >>  8));
        writeByte((int)(v >>  0));
    }

    @Override
    public void writeFloat(float v) throws IOException {
        writeInt(Float.floatToIntBits(v));
    }

    @Override
    public void writeDouble(double v) throws IOException {
        writeLong(Double.doubleToLongBits(v));
    }

    @Override
    public void writeUTF(String s) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeBytes(String s) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeChars(String s) throws IOException {
        throw new UnsupportedOperationException();
    }

}
