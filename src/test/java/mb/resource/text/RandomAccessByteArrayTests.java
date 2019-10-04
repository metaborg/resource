package mb.resource.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests the {@link RandomAccessByteArray} class.
 */
@SuppressWarnings({"WeakerAccess", "ConstantConditions", "CodeBlock2Expr"})
public final class RandomAccessByteArrayTests {

    public static final class constructorTests {

        @Test
        public void constructorWithData_shouldCreateInitialData() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};

            // Act
            RandomAccessByteArray ba = new RandomAccessByteArray(data);
            byte[] result = ba.toArray();

            // Assert
            assertEquals(data.length, ba.getLength());
            assertEquals(data, result);
        }

        @Test
        public void constructorWithCapacity_shouldCreateInitialCapacity() {
            // Arrange
            int desiredCapacity = 30;

            // Act
            RandomAccessByteArray ba = new RandomAccessByteArray(desiredCapacity);

            // Assert
            assertTrue(desiredCapacity <= ba.getCapacity());
        }

    }

    public static final class getLengthTests {

        @Test
        public void getLength_shouldReturnZeroInitially() {
            // Arrange
            RandomAccessByteArray ba = new RandomAccessByteArray();

            // Assert
            assertEquals(0, ba.getLength());
        }

        @Test
        public void getLength_shouldReturnLengthOfWrittenData() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Assert
            assertEquals(data.length, ba.getLength());
        }

        @Test
        public void getLength_shouldReturnLengthOfWrittenDataIncludingPadding() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            int padding = 4;
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Assert
            assertEquals(padding + data.length, ba.getLength());
        }
    }

    public static class setLengthTests {
        @Test
        public void setLengthToSameLength_shouldDoNothing() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Act
            ba.setLength(ba.getLength());

            // Assert
            assertEquals(data.length, ba.getLength());
            assertEquals(data, ba.toArray());
        }

        @Test
        public void setLengthToZero_shouldTruncatesDataToNothing() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Act
            ba.setLength(0);

            // Assert
            assertEquals(0, ba.getLength());
            assertEquals(new byte[0], ba.toArray());
        }

        @Test
        public void setLengthToSmallerThanCurrentLength_shouldTruncateData() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Act
            ba.setLength(4);

            // Assert
            assertEquals(4, ba.getLength());
            assertEquals(new byte[]{0x01, 0x02, 0x03, 0x04}, ba.toArray());
        }

        @Test
        public void setLengthToLargerThanCurrentLength_shouldPadsWithZero() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Act
            ba.setLength(8);

            // Assert
            assertEquals(8, ba.getLength());
            assertEquals(new byte[]{0x01, 0x02, 0x03, 0x04, 0x00, 0x00, 0x00, 0x00}, ba.toArray());
        }

        @Test
        public void setLengthToLargerThanCurrentLength_shouldFixNonZeroBytes() {
            // Arrange
            byte[] data = new byte[]{0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);
            ba.setLength(2);

            // Act
            ba.setLength(8);

            // Assert
            assertEquals(8, ba.getLength());
            assertEquals(new byte[]{0x7F, 0x7F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}, ba.toArray());
        }

        @Test
        public void setLengthToNegative_throwsIllegalArgumentException() {
            // Arrange
            RandomAccessByteArray ba = new RandomAccessByteArray();

            // Assert
            assertThrows(IllegalArgumentException.class, () -> {
                ba.setLength(-1);
            });
        }
    }

    public static class getCapacityTests {

        @Test
        public void getCapacity_isGreaterThanOrEqualToLength() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Assert
            assertTrue(ba.getLength() <= ba.getCapacity());
        }
    }

    public static class readAtTests {
        @Test
        public void readAtZeroNothing_shouldReturnZeroAndKeepsBufferUnchanged() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Act
            byte[] buffer = new byte[]{0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
            int read = ba.readAt(0, buffer, 0, 0);

            // Assert
            assertEquals(0, read);
            assertEquals(new byte[] { 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F}, buffer);
        }

        @Test
        public void readAtZeroSomeBytes_shouldReturnSomeBytes() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Act
            int count = 3;
            byte[] buffer = new byte[count];
            int read = ba.readAt(0, buffer, 0, count);

            // Assert
            assertEquals(count, read);
            assertEquals(new byte[] { 0x01, 0x02, 0x03}, buffer);
        }

        @Test
        public void readAtZeroSomeBytesInLargerBuffer_shouldReturnSomeBytesAndKeepsTheRestAsIs() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Act
            int count = 3;
            byte[] buffer = new byte[]{0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
            int read = ba.readAt(0, buffer, 0, count);

            // Assert
            assertEquals(count, read);
            assertEquals(new byte[] { 0x01, 0x02, 0x03, 0x7F, 0x7F, 0x7F}, buffer);
        }

        @Test
        public void readAtZeroSomeBytesAtBufferOffset_shouldReturnSomeBytesAndKeepsTheRestAsIs() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Act
            int count = 3;
            byte[] buffer = new byte[]{0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
            int read = ba.readAt(0, buffer, 2, count);

            // Assert
            assertEquals(count, read);
            assertEquals(new byte[] { 0x7F, 0x7F, 0x01, 0x02, 0x03, 0x7F}, buffer);
        }

        @Test
        public void readAtZeroSomeBytesWhileDataIsEmpty_shouldReturnEofAndKeepsBufferUnchanged() {
            // Arrange
            RandomAccessByteArray ba = new RandomAccessByteArray();

            // Act
            byte[] buffer = new byte[]{0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
            int read = ba.readAt(0, buffer, 2, 2);

            // Assert
            assertEquals(-1, read);
            assertEquals(new byte[] { 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F}, buffer);
        }

        @Test
        public void readAtZeroMoreBytesThanAvailable_shouldReturnOnlyAvailableBytes() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Act
            byte[] buffer = new byte[]{0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
            int read = ba.readAt(0, buffer, 2, 6);

            // Assert
            assertEquals(4, read);
            assertEquals(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x7F, 0x7F}, buffer);
        }

        @Test
        public void readAtOffsetNothing_shouldReturnZeroAndKeepsBufferUnchanged() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Act
            byte[] buffer = new byte[]{0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
            int read = ba.readAt(2, buffer, 0, 0);

            // Assert
            assertEquals(0, read);
            assertEquals(new byte[] { 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F}, buffer);
        }

        @Test
        public void readAtOffsetSomeBytes_shouldReturnSomeBytes() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Act
            int count = 3;
            byte[] buffer = new byte[count];
            int read = ba.readAt(2, buffer, 0, count);

            // Assert
            assertEquals(count, read);
            assertEquals(new byte[] { 0x03, 0x04, 0x05}, buffer);
        }

        @Test
        public void readAtOffsetSomeBytesInLargerBuffer_shouldReturnSomeBytesAndKeepsTheRestAsIs() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Act
            int count = 3;
            byte[] buffer = new byte[]{0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
            int read = ba.readAt(2, buffer, 0, count);

            // Assert
            assertEquals(count, read);
            assertEquals(new byte[] { 0x03, 0x04, 0x05, 0x7F, 0x7F, 0x7F}, buffer);
        }

        @Test
        public void readAtOffsetSomeBytesAtBufferOffset_shouldReturnSomeBytesAndKeepsTheRestAsIs() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Act
            int count = 3;
            byte[] buffer = new byte[]{0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
            int read = ba.readAt(2, buffer, 2, count);

            // Assert
            assertEquals(count, read);
            assertEquals(new byte[] { 0x7F, 0x7F, 0x03, 0x04, 0x05, 0x7F}, buffer);
        }

        @Test
        public void readAtOffsetMoreBytesThanAvailable_shouldReturnOnlyAvailableBytes() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Act
            byte[] buffer = new byte[]{0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
            int read = ba.readAt(2, buffer, 2, 6);

            // Assert
            assertEquals(2, read);
            assertEquals(new byte[] { 0x01, 0x02, 0x7F, 0x7F, 0x7F, 0x7F}, buffer);
        }

        @Test
        public void readAtEndOfData_shouldReturnEofAndKeepsBufferUnchanged() {
            // Arrange
            byte[] data = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Act
            byte[] buffer = new byte[]{0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
            int read = ba.readAt(data.length, buffer, 0, 2);

            // Assert
            assertEquals(-1, read);
            assertEquals(new byte[] { 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F}, buffer);
        }

        @Test
        public void readAtWithNullBuffer_throwsIllegalArgumentException() {
            // Arrange
            RandomAccessByteArray ba = new RandomAccessByteArray();

            // Assert
            assertThrows(IllegalArgumentException.class, () -> {
                ba.readAt(0, null, 0, 0);
            });
        }

        @Test
        public void readAtWithNegativeOffset_throwsIndexOutOfBoundsException() {
            // Arrange
            RandomAccessByteArray ba = new RandomAccessByteArray();

            // Assert
            assertThrows(IndexOutOfBoundsException.class, () -> {
                ba.readAt(-1, new byte[3], 0, 1);
            });
        }

        @Test
        public void readAtWithNegativeBufferOffset_throwsIndexOutOfBoundsException() {
            // Arrange
            RandomAccessByteArray ba = new RandomAccessByteArray();

            // Assert
            assertThrows(IndexOutOfBoundsException.class, () -> {
                ba.readAt(0, new byte[3], -1, 1);
            });
        }

        @Test
        public void readAtWithNegativeLength_throwsIndexOutOfBoundsException() {
            // Arrange
            RandomAccessByteArray ba = new RandomAccessByteArray();

            // Assert
            assertThrows(IndexOutOfBoundsException.class, () -> {
                ba.readAt(0, new byte[3], 0, -1);
            });
        }

        @Test
        public void readAtWithBufferOffsetPlusLengthOutOfRange_throwsIndexOutOfBoundsException() {
            // Arrange
            RandomAccessByteArray ba = new RandomAccessByteArray();

            // Assert
            assertThrows(IndexOutOfBoundsException.class, () -> {
                ba.readAt(0, new byte[3], 1, 3);
            });
        }
    }

    public static final class writeAtTests {
        @Test
        public void writeNothingAtZero_shouldChangeNothingAndNotIncreaseLength() {
            // Arrange
            byte[] data = new byte[]{0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);
            int currentLength = ba.getLength();
            int currentCapacity = ba.getCapacity();

            // Act
            byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04};
            ba.writeAt(0, buffer, 0, 0);

            // Assert
            assertEquals(currentLength, ba.getLength());
            assertEquals(currentCapacity, ba.getCapacity());
            assertEquals(data, ba.toArray());
        }

        @Test
        public void writeNothingWithinLength_shouldChangeNothingAndNotIncreaseLength() {
            // Arrange
            byte[] data = new byte[]{0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);
            int currentLength = ba.getLength();
            int currentCapacity = ba.getCapacity();

            // Act
            ba.writeAt(4, new byte[0], 0, 0);

            // Assert
            assertEquals(currentLength, ba.getLength());
            assertEquals(currentCapacity, ba.getCapacity());
            assertEquals(data, ba.toArray());
        }

        @Test
        public void writeNothingOutsideLengthWithinCapacity_shouldIncreaseLengthAndZeroPad() {
            // Arrange
            byte[] data = new byte[]{0x7F, 0x7F};
            RandomAccessByteArray ba = new RandomAccessByteArray(8);
            ba.writeAt(0, data, 0, data.length);
            int currentCapacity = ba.getCapacity();

            // Act
            ba.writeAt(4, new byte[0], 0, 0);

            // Assert
            assertEquals(4, ba.getLength());
            assertEquals(currentCapacity, ba.getCapacity());
            assertEquals(new byte[]{0x7F, 0x7F, 0x00, 0x00}, ba.toArray());
        }

        @Test
        public void writeNothingOutsideCapacity_shouldIncreaseLengthAndCapacityAndZeroPad() {
            // Arrange
            byte[] data = new byte[]{0x7F, 0x7F};
            RandomAccessByteArray ba = new RandomAccessByteArray(4);
            ba.writeAt(0, data, 0, data.length);
            int currentCapacity = ba.getCapacity();

            // Act
            ba.writeAt(8, new byte[0], 0, 0);

            // Assert
            assertEquals(8, ba.getLength());
            assertTrue(currentCapacity < ba.getCapacity());
            assertEquals(new byte[]{0x7F, 0x7F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}, ba.toArray());
        }


        @Test
        public void writeSomethingAtZeroWithinExistingData_shouldChangeExistingDataButNotIncreaseLength() {
            // Arrange
            byte[] data = new byte[]{0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);
            int currentLength = ba.getLength();
            int currentCapacity = ba.getCapacity();

            // Act
            byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04};
            ba.writeAt(0, buffer, 1, 2);

            // Assert
            assertEquals(currentLength, ba.getLength());
            assertEquals(currentCapacity, ba.getCapacity());
            assertEquals(new byte[]{0x02, 0x03, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F}, ba.toArray());
        }

        @Test
        public void writeSomethingAtZeroWithinCapacity_shouldChangeDataAndIncreaseLengthButNotIncreaseCapacity() {
            // Arrange
            byte[] data = new byte[]{0x7F};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);
            int currentCapacity = ba.getCapacity();

            // Act
            byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04};
            ba.writeAt(0, buffer, 0, 4);

            // Assert
            assertEquals(5, ba.getLength());
            assertEquals(currentCapacity, ba.getCapacity());
            assertEquals(new byte[]{0x01, 0x02, 0x03, 0x04}, ba.toArray());
        }

        @Test
        public void writSomethingAtZeroOutsideCapacity_shouldChangeDataAndIncreaseLengthAndIncreaseCapacity() {
            // Arrange
            byte[] data = new byte[0];
            RandomAccessByteArray ba = new RandomAccessByteArray(data);
            int currentCapacity = ba.getCapacity();

            // Act
            byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            ba.writeAt(0, buffer, 0, 8);

            // Assert
            assertEquals(8, ba.getLength());
            assertTrue(currentCapacity < ba.getCapacity());
            assertEquals(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08}, ba.toArray());
        }




        @Test
        public void writeSomethingWithinLength_shouldChangeExistingDataButNotIncreaseLength() {
            // Arrange
            byte[] data = new byte[]{0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);
            int currentLength = ba.getLength();
            int currentCapacity = ba.getCapacity();

            // Act
            byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04};
            ba.writeAt(0, buffer, 1, 2);

            // Assert
            assertEquals(currentLength, ba.getLength());
            assertEquals(currentCapacity, ba.getCapacity());
            assertEquals(new byte[]{0x02, 0x03, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F}, ba.toArray());
        }

        @Test
        public void writeSomethingUntilOutsideLengthWithinCapacity_shouldChangeExistingDataAndIncreaseLength() {
            // Arrange
            byte[] data = new byte[]{0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);
            ba.setLength(4);
            int currentCapacity = ba.getCapacity();

            // Act
            byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04};
            ba.writeAt(2, buffer, 0, 4);

            // Assert
            assertEquals(6, ba.getLength());
            assertEquals(currentCapacity, ba.getCapacity());
            assertEquals(new byte[]{0x7F, 0x7F, 0x01, 0x02, 0x03, 0x04}, ba.toArray());
        }

        @Test
        public void writeSomethingWithinCapacity_shouldChangeExistingDataAndIncreaseLength() {
            // Arrange
            byte[] data = new byte[]{0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F, 0x7F};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);
            ba.setLength(2);
            int currentCapacity = ba.getCapacity();

            // Act
            byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04};
            ba.writeAt(4, buffer, 0, 4);

            // Assert
            assertEquals(6, ba.getLength());
            assertEquals(currentCapacity, ba.getCapacity());
            assertEquals(new byte[]{0x7F, 0x7F, 0x00, 0x00, 0x01, 0x02, 0x03, 0x04}, ba.toArray());
        }

        @Test
        public void writeSomethingUntilOutsideCapacity_shouldIncreaseLengthAndCapacity() {
            // Arrange
            byte[] data = new byte[]{0x7F, 0x7F};
            RandomAccessByteArray ba = new RandomAccessByteArray(4);
            ba.writeAt(0, data, 0, data.length);
            int oldCapacity = ba.getCapacity();

            // Act
            byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            ba.writeAt(1, buffer, 0, 4);

            // Assert
            assertEquals(9, ba.getLength());
            assertTrue(oldCapacity < ba.getCapacity());
            assertEquals(new byte[]{0x7F, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08}, ba.toArray());
        }

        @Test
        public void writeSomethingStartingOutsideCapacity_shouldZeroPadAndIncreaseLengthAndCapacity() {
            // Arrange
            byte[] data = new byte[]{0x7F, 0x7F};
            RandomAccessByteArray ba = new RandomAccessByteArray(4);
            ba.writeAt(0, data, 0, data.length);
            int oldCapacity = ba.getCapacity();

            // Act
            byte[] buffer = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08};
            ba.writeAt(4, buffer, 0, 4);

            // Assert
            assertEquals(12, ba.getLength());
            assertTrue(oldCapacity < ba.getCapacity());
            assertEquals(new byte[]{0x7F, 0x7F, 0x00, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08}, ba.toArray());
        }



        @Test
        public void writeAtWithNullBuffer_throwsIllegalArgumentException() {
            // Arrange
            RandomAccessByteArray ba = new RandomAccessByteArray();

            // Assert
            assertThrows(IllegalArgumentException.class, () -> {
                ba.writeAt(0, null, 0, 0);
            });
        }

        @Test
        public void writeAtWithNegativeOffset_throwsIndexOutOfBoundsException() {
            // Arrange
            RandomAccessByteArray ba = new RandomAccessByteArray();

            // Assert
            assertThrows(IndexOutOfBoundsException.class, () -> {
                ba.writeAt(-1, new byte[3], 0, 1);
            });
        }

        @Test
        public void writeAtWithNegativeBufferOffset_throwsIndexOutOfBoundsException() {
            // Arrange
            RandomAccessByteArray ba = new RandomAccessByteArray();

            // Assert
            assertThrows(IndexOutOfBoundsException.class, () -> {
                ba.writeAt(0, new byte[3], -1, 1);
            });
        }

        @Test
        public void writeAtWithNegativeLength_throwsIndexOutOfBoundsException() {
            // Arrange
            RandomAccessByteArray ba = new RandomAccessByteArray();

            // Assert
            assertThrows(IndexOutOfBoundsException.class, () -> {
                ba.writeAt(0, new byte[3], 0, -1);
            });
        }

        @Test
        public void writeAtWithBufferOffsetPlusLengthOutOfRange_throwsIndexOutOfBoundsException() {
            // Arrange
            RandomAccessByteArray ba = new RandomAccessByteArray();

            // Assert
            assertThrows(IndexOutOfBoundsException.class, () -> {
                ba.writeAt(0, new byte[3], 1, 3);
            });
        }

    }


    public static class toArrayTests {
        @Test
        public void emptyData_shouldReturnEmptyArray() {
            // Arrange
            RandomAccessByteArray ba = new RandomAccessByteArray();

            // Act
            byte[] result = ba.toArray();

            // Assert
            assertEquals(new byte[0], result);
        }

        @Test
        public void nonEmptyData_shouldReturnSameData() {
            // Arrange
            byte[] data = {0x01, 0x02, 0x03, 0x04};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);

            // Act
            byte[] result = ba.toArray();

            // Assert
            assertEquals(data, result);
        }

        @Test
        public void shouldNotReturnBytesOutsideLength() {
            // Arrange
            byte[] data = {0x01, 0x02, 0x03, 0x04};
            RandomAccessByteArray ba = new RandomAccessByteArray(data);
            ba.setLength(16);
            ba.setLength(4);

            // Act
            byte[] result = ba.toArray();

            // Assert
            assertEquals(data, result);
        }
    }

}
