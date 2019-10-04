package mb.resource.text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.ClosedChannelException;
import java.util.Arrays;
import java.util.function.Consumer;


public class CloseableByteArrayOutputStream extends ByteArrayOutputStream {

    private boolean closed = false;
    private final Consumer<ByteArrayOutputStream> onClose;

    /**
     * Initializes a new instance of the {@link CloseableByteArrayOutputStream} class.
     * @param onClose
     */
    public CloseableByteArrayOutputStream(Consumer<ByteArrayOutputStream> onClose) {
        this.onClose = onClose;
    }

    @Override
    public synchronized void close() throws IOException {
        this.closed = true;
        this.onClose.accept(this);
    }

    /**
     * Ensures the stream is not closed.
     * @throws ClosedChannelException The stream is closed.
     */
    private void ensureNotClosed() throws ClosedChannelException {
        if (this.closed)
            throw new ClosedChannelException();
    }

    /**
     * Ensures the stream is not closed.
     * @throws RuntimeException The stream is closed.
     */
    private void ensureNotClosedRuntime() {
        try {
            ensureNotClosed();
        } catch (ClosedChannelException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public synchronized void write(int b) {
        ensureNotClosedRuntime();
        super.write(b);
    }

    @Override
    public synchronized void write(byte[] b, int off, int len) {
        ensureNotClosedRuntime();
        super.write(b, off, len);
    }

    @Override
    public synchronized void writeTo(OutputStream out) throws IOException {
        ensureNotClosed();
        super.writeTo(out);
    }

    @Override
    public synchronized void reset() {
        ensureNotClosedRuntime();
        super.reset();
    }
}

