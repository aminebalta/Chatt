package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;
import pdu.PaddingException;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.*;

public class PARTICIPANTS extends PDU {

    Collection<String> client = new HashSet<>();

    int numClients;
    private Iterable<? extends String> clientNames;

    public PARTICIPANTS(InputStream is) throws IOException, PaddingException {

        numClients = is.read();

        short cliLength = (short) byteArrayToLong(readExactly(is, 2));
        byte[] clientByte = readExactly(is, cliLength);
        String[] clientNames = new String(clientByte, "UTF-8").split("\u0000");

        for (String clientName : clientNames){
            client.add(clientName);
        }
        checkPaddning(is, padLengths(cliLength)- cliLength);

    }

    public PARTICIPANTS(String[] clientNames){
        for (String cli: clientNames){
            client.add(cli);
        }
        numClients = client.size();
    }

    public Collection<String> getClients() {
        return client;
    }

    @Override
    public byte[] toByteArray() {

        byte[] clientByte = null;
        short clientSize = 0;

        ByteSequenceBuilder bsb = new ByteSequenceBuilder(OpCode.PARTICIPANTS.value, (byte)client.size());
        for (Iterator<String> iterator = client.iterator(); iterator.hasNext();) {
            String clients = iterator.next();
            clientSize += (short) (clients.length() + 1);
        }

        bsb.appendShort((short) clientSize);

        for (Iterator<String> iterator = client.iterator(); iterator.hasNext();) {
            clientByte = iterator.next().getBytes(UTF_8);
            bsb.append(clientByte);
            bsb.append((byte) 0);
        }
        return bsb.pad().toByteArray();

    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + ((client == null) ? 0 : client.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object object) {

        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        PARTICIPANTS other = (PARTICIPANTS) object;
        if (client == null) {
            if (other.client != null)
                return false;
        } else if (!client.equals(other.client)) ;

        return true;
    }
    @Override
    public OpCode getOpCode(){
        return OpCode.PARTICIPANTS;
    }
}