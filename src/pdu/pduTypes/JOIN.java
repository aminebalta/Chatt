package pdu.pduTypes;

import pdu.PDU;
import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PaddingException;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class JOIN extends PDU {

    private String nickname = "";

    public JOIN(String nickname) {
        this.nickname = nickname;
    }

    /*Unpacks the PDU*/
    public JOIN(InputStream is) throws IOException, PaddingException {
        DataInputStream dataStream = new DataInputStream(is);
        int nicknameLength = dataStream.read();
        checkPaddning(dataStream, 2);


        byte[] nickBytes = readExactly(dataStream, nicknameLength);
        nickname = new String(nickBytes, StandardCharsets.UTF_8);
        checkPaddning(dataStream, padLengths(nicknameLength) - nicknameLength);
    }

    /*Packs the PDU*/
    @Override
    public byte[] toByteArray() {
        byte[] nicknameBytes = nickname.getBytes(StandardCharsets.UTF_8);

        return new ByteSequenceBuilder(OpCode.JOIN.value, (byte)
                nicknameBytes.length).pad().append(nicknameBytes).pad().toByteArray();

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nickname == null) ? 0 :nickname.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        JOIN other = (JOIN) object;
        if (nickname == null) {
            if (other.nickname != null) {
                return false;
            }
        } else if (!nickname.equals(other.nickname)) {
            return false;
        }
        return true;
    }

    @Override
    public OpCode getOpCode(){
        return OpCode.JOIN;
    }
}
