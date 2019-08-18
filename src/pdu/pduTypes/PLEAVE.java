package pdu.pduTypes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import pdu.PDU;
import pdu.OpCode;

import pdu.ByteSequenceBuilder;
import pdu.PaddingException;

import java.nio.charset.StandardCharsets;

public class PLEAVE extends PDU {

    private String nickname = "";
    private Date timestamp = new Date();

    public PLEAVE(String nickname, Date timestamp) {
        this.nickname = nickname;
        this.timestamp.setTime(timestamp.getTime() / 1000 * 1000);
    }

    public PLEAVE(InputStream is) throws IOException, PaddingException {
        int nickLenght = is.read();
        checkPaddning(is, 2);
        timestamp.setTime(byteArrayToLong(readExactly(is, 4)) * 1000);
        nickname = new String(readExactly(is, nickLenght),
                StandardCharsets.UTF_8);
        checkPaddning(is, padLengths(nickLenght) - nickLenght);
    }

    @Override
    public byte[] toByteArray(){
        ByteSequenceBuilder bsb = new ByteSequenceBuilder(OpCode.PLEAVE.value);
        bsb.append((byte) nickname.getBytes().length).pad();
        bsb.appendInt((int) timestamp.getTime() / 1000);

        byte[] nicknameBytes = nickname.getBytes(StandardCharsets.UTF_8);
        bsb.append(nicknameBytes).pad();

        return  bsb.toByteArray();
    }
    public String getNickname() {
        return nickname;
    }
    public Date getTimestamp(){
        return timestamp;
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object object){
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }
        PLEAVE other = (PLEAVE) object;
        if (nickname == null) {
            if (other.nickname != null) {
                return false;
            }
        } else if (!nickname.equals(other.nickname)) {
            return false;
        }
        if (timestamp == null){
            if(other.timestamp != null){
                return false;
            }
        }else if (!timestamp.equals(other.timestamp)){
            return false;
        }
        return true;
    }
    @Override
    public OpCode getOpCode(){
        return OpCode.PLEAVE;
    }
}