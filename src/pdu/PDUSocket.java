package pdu;

import pdu.pduTypes.GETLIST;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Amine Balta and Matilda Nilsson on 2017-12-20.
 */
public class PDUSocket {
    private InputStream inputStream;
    private OutputStream outputStream;

    public PDUSocket(String address, int port) throws IOException {

        Socket socket = new Socket(address, port);

        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    public void writePDU(PDU pdu) throws IOException {
        outputStream.write(pdu.toByteArray());
    }

    public PDU readPDU() throws IOException, ChecksumException, PaddingException {
        return PDU.fromInputStream(inputStream);
    }
}
