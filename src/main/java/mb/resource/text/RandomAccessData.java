package mb.resource.text;

import java.io.*;


public final class RandomAccessData extends DataInputOutput {

    private volatile boolean closed = false;
    private byte[] data;
    private int cursor = 0;

    public RandomAccessData(byte[] content) {
        // TODO: Safety copy?
        this.data = content;
    }

    @Override
    public int peek() throws IOException {
        if (this.cursor < 0 || this.cursor >= this.data.length)
            return EOF;
        return this.data[this.cursor];
    }

    @Override
    public int read() throws IOException {
        int b = peek();
        if (b != EOF) this.cursor += 1;
        return b;
    }

    @Override
    public void write(int b) throws IOException {

    }

    @Override
    public int skipBytes(int n) throws IOException {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }

}
