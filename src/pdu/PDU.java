package pdu;

        import java.io.IOException;
        import java.io.InputStream;
        import pdu.pduTypes.*;
        //import sun.security.util.Length;

/**
 * Super class of all PDUs with methods for reading PDUs from InputStreams.
 */
public abstract class PDU {

    /**
     * Reads the OpCode from the InputStream and determines the type of
     * the PDU. Then a PDU of the correct subclass is read from the stream.
     *
     * @param is The InputStream to read the PDU from.
     * @return The read PDU.
     * @throws java.io.IOException If an IOException was thrown when reading from the
     *          stream.
     * @throws IllegalArgumentException If the first byte of the stream
     *          doesn't represent a correct OpCode.
     */
    public static PDU fromInputStream(InputStream is) throws IOException, ChecksumException, PaddingException {
        byte opnr = (byte) is.read();

        if (opnr == -1) throw new IOException();

        switch (opnr) {
            case 3:
                return new GETLIST(is);
            case 4:
                return new SLIST(is);
            case 10:
                return new MESS(is);
            case 11:
                return new QUIT(is);
            case 12:
                return new JOIN(is);
            case 16:
                return new PJOIN(is);
            case 17:
                return new PLEAVE(is);
            case 19:
                return new PARTICIPANTS(is);
        }
        return null;
    }

    public abstract OpCode getOpCode();

    /**
     * @return A byte array representation of the PDU with a length divisible
     * by 4.
     */
    public abstract byte[] toByteArray();

    /**
     * Computes the sum of the specified lengths, padding each
     * individually to be divisible by 4.
     *
     * @param lengths The lengths to pad.
     * @return The padded sum.
     */
    public static int padLengths(int... lengths) {
        int result = 0;
        for (int length : lengths) {
            if (length % 4 != 0) {
                result += length + (4 - (length % 4));
            } else {
                result += length;
            }
        }
        return result;
    }

    /**
     * Reads exactly the specified amount of bytes from the stream, blocking
     * until they are available even though some bytes are.
     *
     * @param is The InputStream to read from.
     * @param len The number of bytes to read.
     * @return A byte array containing the read bytes.
     * @throws IllegalArgumentException If the number of bytes to read is
     *                      negative.
     * @throws java.io.IOException If an IOException was thrown when reading from the
     *                      stream.
     */
    public static byte[] readExactly(InputStream is, int len)
            throws IOException {

        if (len < 0) {
            throw new IllegalArgumentException("Negative length to read");
        }
        byte[] buffer = new byte[len];

        int readCount = 0;
        while (readCount < len) {
            int readBytes = is.read(buffer, readCount, len - readCount);
            readCount += readBytes;
        }
        return buffer;
    }

    public static long byteArrayToLong(byte[] bytes) {
        return byteArrayToLong(bytes, 0, bytes.length);
    }

    public static long byteArrayToLong(byte[] bytes, int start, int stop) {
        if (stop - start > 8) {
            throw new IllegalArgumentException(
                    "Byte array can not have more than 8 bytes");
        }
        long result = 0;
        for (int i = start; i < stop; i++) {
            result <<= 8;
            result += ((long) bytes[i]) & 0xff;
        }
        return result;
    }
    public void checkPaddning (InputStream is, int len) throws IOException, PaddingException {
        for (int i = 0; i < len; i++){
            byte pad = (byte)is.read();
                if (pad != 0x0)
                    throw new PaddingException();
        }
    }
}