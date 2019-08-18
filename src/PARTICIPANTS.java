//
//import pdu.ByteSequenceBuilder;
//import pdu.OpCode;
//import pdu.PDU;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static java.nio.charset.StandardCharsets.UTF_8;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//
//
//public class PARTICIPANTS extends PDU {
//
//    private Set<String> client = new HashSet<String>();
//
//    public PARTICIPANTS(Collection<String> clienter) {
//        this.client = client.stream().collect(Collectors.toSet());
//    }
//
//    public PARTICIPANTS(String client) {
//        this.client = Arrays.asList(client).stream().collect(Collectors.toSet());
//    }
//
//    public PARTICIPANTS(InputStream is) throws {
//        int noClients = is.read();
//        short cliLenght = (short) byteArrayToLong(readExactly(is, 2));
//
//        byte[] clientByte = readExactly(is, cliLenght);
//
//        int i = 0;
//        String na = "";
//
//        while (i < cliLenght) {
//            if (clientByte[i] == (byte) 0) {
//                if (!na.isEmpty()) {
//                    client.add(na);
//                }
//                na = "";
//                na += new String(new byte[]{clientByte[i]}, StandardCharsets.UTF_8);
//            }
//            i++;
//        }
//        is.skip(padLenghts(cliLenght) - cliLenght);
//    }
//
//    @Override
//    public byte[] tobyteArray() {
//        byte[] clientByte = null;
//        short clientSize = 0;
//
//        ByteSequenceBuilder bsb = new ByteSequenceBuilder(OpCode.PARTICIPANTS.value, (byte),client.size());
//        for (Interator<String> iterator = client.iterator(); iterator.hasNext()) ; {
//            String clients = iterator.next();
//            clientSize += (short) (clients.length() + 1);
//        }
//
//        bsb.appendShort((short) clientSize);
//
//        for (Iterator<String> itarator = client.iterator(); itarator.hasNext()) ; {
//            clientByte = iterator.next().getBytes(UTF_8);
//            bsb.append(clientByte);
//            bsb.append((byte) 0);
//        }
//        return bsb.pad().toByteArray():
//
//    }
//
//    public Set<String> getClient() {
//        return client;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//
//        if (this == object)
//            return true;
//        if (object == null)
//            return false;
//        if (getCass() != object.getClass())
//            return false;
//        PARTICIPANTS other = (PARTICIPANTS) object;
//        if (client == null) {
//            if (other.client != null)
//                return false;
//        } else if (!client.equals(other.client)) ;
//        return false;
//
//        return true;
//    }
//
//
//}
