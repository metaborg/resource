package mb.resource.text;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * A random access byte array.
 */
public final class RandomAccessByteArray {

    /** The minimum capacity. */
    private static final int MINIMUM_CAPACITY = 4;
    private static final int EOF = -1;
    /** The data array. It's capacity >= dataLength. */
    private byte[] data;
    /** The number of valid bytes in the data array. */
    private int length;
    /** Read/write lock. */
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Initializes a new instance of the {@link RandomAccessByteArray} class.
     */
    public RandomAccessByteArray() {
        this(MINIMUM_CAPACITY);
    }

    /**
     * Initializes a new instance of the {@link RandomAccessByteArray} class.
     * @param initialData The initial data.
     */
    public RandomAccessByteArray(byte[] initialData) {
        this(initialData.length);
        writeAt(0, initialData, 0, initialData.length);
    }

    /**
     * Initializes a new instance of the {@link RandomAccessByteArray} class.
     * @param initialCapacity The minimum initial capacity.
     */
    public RandomAccessByteArray(int initialCapacity) {
        // Don't need a lock in the constructor.
        ensureCapacity(initialCapacity);
    }

    /**
     * Gets the length of the data.
     * @return The length of the data, in bytes.
     */
    public int getLength() {
        Lock lock = this.lock.readLock();
        try {
            return this.length;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Sets the length of the data.
     *
     * Increasing the length of the data pads it with null bytes.
     *
     * @param length The length of the data, in bytes.
     */
    public void setLength(int length) {
        Lock lock = this.lock.writeLock();
        try {
            ensureCapacity(length);
            if (this.length < length) {
                // Ensure the bytes between the original length and the new length are zeroed out.
                Arrays.fill(this.data, this.length, length, (byte)0);
            }
            this.length = length;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Gets the capacity of the byte array.
     *
     * @return The capacity, in bytes.
     */
    public int getCapacity() {
        Lock lock = this.lock.readLock();
        try {
            return this.data.length;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Reads from the specified offset in the data.
     * @param offset The offset at which to start reading. This may be past the end of the data,
     *               in which case the function returns -1.
     * @param buffer The buffer to which to write the data.
     * @param bufferOffset The offset in the buffer at which to start writing.
     * @param length The number of bytes to read.
     * @return The number of bytes read, or -1 when we reached the end of the data.
     */
    public int readAt(int offset, byte[] buffer, int bufferOffset, int length) {
        if (offset < 0) throw new IndexOutOfBoundsException("offset is must be positive or zero.");
        if (buffer == null) throw new IllegalArgumentException("buffer must not be null.");
        if (bufferOffset < 0 || length < 0 || bufferOffset + length > buffer.length) throw new IndexOutOfBoundsException("bufferOffset or length are out of buffer array bounds.");

        Lock lock = this.lock.readLock();
        try {
            int toCopy = Math.min(length, this.length - offset);
            if (toCopy == 0) return EOF;
            System.arraycopy(this.data, offset, buffer, bufferOffset, toCopy);
            return toCopy;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Writes to the specified offset in the data.
     * @param offset The offset at which to start writing. This may be past the end of the data,
     *               in which case the space in between will be filled with zeroes.
     * @param buffer The buffer from which to read the data.
     * @param bufferOffset The offset in the buffer at which to start reading.
     * @param length The number of bytes to write.
     */
    public void writeAt(int offset, byte[] buffer, int bufferOffset, int length) {
        if (offset < 0) throw new IndexOutOfBoundsException("offset is must be positive or zero.");
        if (buffer == null) throw new IllegalArgumentException("buffer must not be null.");
        if (bufferOffset < 0 || length < 0 || bufferOffset + length > buffer.length) throw new IndexOutOfBoundsException("bufferOffset or length are out of buffer array bounds.");

        Lock lock = this.lock.writeLock();
        try {
            ensureCapacity(offset + length);
            if (this.length < offset) {
                // Ensure the bytes between the original length and the new length are zeroed out.
                Arrays.fill(this.data, this.length, offset, (byte)0);
            }
            System.arraycopy(buffer, bufferOffset, this.data, offset, length);
            this.length = offset + length;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Ensures the data array has at least of the specified capacity.
     *
     * This method may only be called in a write lock.
     *
     * @param capacity The minimum capacity.
     */
    private void ensureCapacity(int capacity) {
        int newCapacity = Math.max((this.data != null ? this.data.length : 0), MINIMUM_CAPACITY);;
        while (newCapacity < capacity) {
            // We double the capacity each time.
            newCapacity *= 2;
        }
        if (this.data.length < newCapacity) {
            byte[] newData = new byte[newCapacity];
            // Copy only the valid data.
            System.arraycopy(this.data, 0, newData, 0, length);
            this.data = newData;
        }
    }

    /**
     * Copies the data to an array.
     * @return The array.
     */
    public byte[] toArray() {
        Lock lock = this.lock.readLock();
        try {
            byte[] array = new byte[this.length];
            readAt(0, array, 0, array.length);
            return array;
        } finally {
            lock.unlock();
        }
    }

}
