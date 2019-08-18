package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.OpCode;
import pdu.PDU;
import pdu.PaddingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.nio.charset.StandardCharsets;

public class PJOIN extends PDU {
    private String nickname  = "";
    private Date timestamp = new Date();

    public PJOIN(String nickname, Date timestamp) {
        this.nickname = nickname;
        this.timestamp.setTime(timestamp.getTime() / 100 * 1000);
    }
    public PJOIN(InputStream is) throws IOException, PaddingException {
        int nicknameLength = is.read();
        checkPaddning(is, 2);

        this.timestamp.setTime(byteArrayToLong(readExactly(is, 4)) * 1000);
        byte [] nicknameBytes = readExactly(is, nicknameLength);
        this.nickname = new String(nicknameBytes, StandardCharsets.UTF_8);

        checkPaddning(is, padLengths(nicknameLength) - nicknameLength);
    }

    public String getNickname(){
        return nickname;
    }
    public Date getTimestamp(){
        return timestamp;
    }
    @Override
    public byte[] toByteArray() {
        byte[] nicknameBytes = nickname.getBytes(StandardCharsets.UTF_8);

        ByteSequenceBuilder bsb = new ByteSequenceBuilder(OpCode.JOIN.value,
                (byte) nicknameBytes.length).pad();

        /*Milliseconds to seconds*/
        int seconds = (int)(timestamp.getTime() / 1000);
        bsb.appendInt(seconds);
        bsb.append(nicknameBytes).pad();

        return bsb.toByteArray();
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
        PJOIN other = (PJOIN) object;
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
        return OpCode.PJOIN;
    }
}

