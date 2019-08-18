package pdu.pduTypes;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.nio.charset.StandardCharsets;

import pdu.*;

/**
 * Message between client and server, can be sent from both client and server.
 * Is a PDU with nickname, message and a timestamp.
 */

public class MESS extends PDU{


    private Date timestamp;
    private String mess = "";
    private String nickname = "";
    byte checking = 0;

    public MESS (String mess){
        this.mess = mess;
        this.timestamp = new Date((System.currentTimeMillis()/ 1000 * 1000));
    }

    public MESS(InputStream is) throws IOException, ChecksumException, PaddingException {
        checkPaddning(is, 1);

        int nicknameLenght = is.read();
        checking = (byte) is.read();
        int messLenght = (int) byteArrayToLong(readExactly(is, 2));
        checkPaddning(is, 2);

        //timestamp.setTime(byteArrayToLong(readExactly(is, 4)) * 1000);
        timestamp = new Date(byteArrayToLong(readExactly(is, 4)) * 1000);

        byte[] messByte = readExactly(is, messLenght);
        mess = new String (messByte, StandardCharsets.UTF_8);
        checkPaddning(is, padLengths(messLenght)-messLenght);

        byte[] nicknameByte = readExactly (is, nicknameLenght);

        nickname = new String (nicknameByte, StandardCharsets.UTF_8);
        checkPaddning(is, padLengths(nicknameLenght) - nicknameLenght);


        byte[] checksumSave = this.toByteArray();
        checksumSave[3] = checking;
        // Checks the checksum
        if(Checksum.computeChecksum(checksumSave) != 0x0){
            throw new ChecksumException();
        }
    }

    public String getMess(){return mess; }

    public String getNickname(){ return nickname; }

    public String getDate(){
        DateFormat df = new SimpleDateFormat(" " + "yyyy-mm-dd hh:mm:ss" + " ");
        String date = df.format(timestamp);
        return date;
    }

    @Override
    public byte [] toByteArray(){

        byte[] messByte = this.mess.getBytes(StandardCharsets.UTF_8);
        byte[] nicknameByte = this.nickname.getBytes(StandardCharsets.UTF_8);

        //checking = 0;

        // BSB saved in variable
        ByteSequenceBuilder bsb = new ByteSequenceBuilder().append(OpCode.MESS.value)
                .append((byte) 0)
                .append((byte) nicknameByte.length)
                .pad()
                .appendShort((byte) messByte.length)
                .pad()
                .pad()
                .appendInt((int)(timestamp.getTime()/1000))
                .append(messByte);

        for(int i = 0; i < (padLengths(messByte.length)-(messByte.length)); i++ ){
            bsb.pad();
        }
        bsb.append(nicknameByte);

        for(int i = 0; i < (padLengths(nicknameByte.length)-(nicknameByte.length)); i++ ) {
            bsb.pad();
        }

        byte [] outStream = bsb.toByteArray();
        outStream[3] = Checksum.computeChecksum(outStream);
        return outStream;

    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mess == null) ? 0 : mess.hashCode());
        result = prime * result + ((nickname == null) ? 0 : nickname.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        return result;
    }

    @Override
    public boolean equals (Object object){

        if (this == object)
            return true;
        if(object == null)
            return false;
        if(getClass() != object.getClass())
            return false;
        MESS other = (MESS) object;
        if (mess == null){
            if(other.mess != null)
                return false;
        }
        else if (!mess.equals(other.mess))
            return false;
        if(nickname == null) {
            if(other.nickname != null)
                return false;
        }
        else if (!nickname.equals(other.nickname))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        }
        else if (!timestamp.equals(other.timestamp))
            return false;

        return true;
    }

    @Override
    public OpCode getOpCode() {
        return OpCode.MESS;
    }

}