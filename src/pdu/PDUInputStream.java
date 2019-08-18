package pdu;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import pdu.PDU;

public class PDUInputStream {

    public PDUInputStream(InputStream inputStream) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * @return The next PDU in the stream.
     * @throws java.io.EOFException If the stream closed without an error.
     * @throws java.io.IOException If the stream closed with an error.
     */
    public PDU readPdu() throws EOFException, IOException {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}