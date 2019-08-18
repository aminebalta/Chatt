package pdu.pduTypes;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

import pdu.*;

public class SLIST extends PDU {

    private ServerEntry[] entries;

    public SLIST(InputStream is) throws IOException, PaddingException {

        checkPaddning(is, 1);

        short serverCount = (short) byteArrayToLong(readExactly(is, 2));

        entries = new ServerEntry[serverCount];

        for (int i = 0; i < serverCount; i++) {

            InetAddress address;
            short port;
            byte clientCount;
            String serverName;

            //Read address
            address = InetAddress.getByAddress(readExactly(is, 4));

            //Read port
            port = (short) byteArrayToLong(readExactly(is, 2));

            //Read numbers of clients
            clientCount = (byte) is.read();

            //Read name
            int serverNameLen = is.read();
            byte[] serverBytes = readExactly(is, serverNameLen);

            switch (serverNameLen % 4) {
                case 0:
                    break;
                case 1:
                    //is.skip(3);
                    checkPaddning(is, 3);
                    break;
                case 2:
                    //is.skip(2);
                    checkPaddning(is, 2);
                    break;
                case 3:
                    //is.skip(1);
                    checkPaddning(is, 1);
            }
            serverName = new String(serverBytes, StandardCharsets.UTF_8);
            ServerEntry count = new ServerEntry(address, port, clientCount, serverName);
            entries[i] = count;
        }
    }

    public ServerEntry[] getEntries(){
        return entries;
    }

    @Override
    public OpCode getOpCode() {
        return OpCode.SLIST;
    }

    @Override
    public byte[] toByteArray() {


        ByteSequenceBuilder bsb = new ByteSequenceBuilder(OpCode.SLIST.value, (byte) 0).appendShort((short) entries.length);

        for (ServerEntry count : entries) {


            byte[] serverNameBytes = count.serverName.getBytes(StandardCharsets.UTF_8);
            bsb.append(count.address.getAddress()).appendShort(count.port).append(count.clientCount).append((byte) serverNameBytes.length).append(serverNameBytes).pad();
        }
        return bsb.toByteArray();
    }

    public ServerEntry[] getServerCount() {

        return entries;
    }

    public static class ServerEntry {

        public final String serverName;
        public final InetAddress address;
        public final short port;
        public final byte clientCount;

        public ServerEntry(InetAddress address, short port, byte clientCount, String serverName) {

            this.serverName = serverName;
            this.address = address;
            this.port = port;
            this.clientCount = clientCount;
        }


        @Override
        public String toString() {
            return "ServerEntry{" + "serverName='" + serverName + ", address" + address + ", port" + port + ", clientCount" + clientCount + '}';
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;

            ServerEntry that = (ServerEntry) obj;

            if (port != that.port) {
                System.out.println(port);
                return false;
            }
            if (clientCount != that.clientCount) {
                System.out.println(clientCount);
                return false;
            }
            if (serverName != null ? !serverName.equals(that.serverName) : that.serverName != null)
                return false;
            return !(address != null ? !address.equals(that.address) : that.address != null);
        }

        @Override
        public int hashCode() {

            int result = serverName != null ? serverName.hashCode() : 0;
            result = 31 * result + (address != null ? address.hashCode() : 0);
            result = 31 * result + (int) port;
            result = 31 * result + (int) clientCount;
            return result;
        }
    }

    @Override
    public String toString() {
        return "SLIST{" + ", entries=" + Arrays.toString(entries) + '}';
    }

}


