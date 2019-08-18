package pdu.pduTypes;

import org.junit.Assert;
import org.junit.Test;
import pdu.OpCode;
import pdu.PDU;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by aminebalta on 2018-05-14.
 */
public class PLEAVETest {

    private static Date time = new Date(6373898);
    private static String participant = "Gustav ";
    private PLEAVE pleave;

    public PLEAVETest(){
        this.pleave = new PLEAVE(participant, time);
    }

    @Test
    public void getNickname() throws Exception {
        byte[] pduBytes = this.pleave.toByteArray();
        byte[] nickBytes = new byte[pduBytes[1]];
        System.arraycopy(pduBytes, 8, nickBytes, 0, nickBytes.length);
        Assert.assertEquals("Gustav ", new String(nickBytes, StandardCharsets.UTF_8));

    }

    @Test
    public void nickLen() throws Exception{
        Assert.assertEquals((long)"Gustav ".getBytes(StandardCharsets.UTF_8).length,(long)this.pleave.toByteArray()[1]);
    }

    @Test
    public void equals() throws Exception {
        Assert.assertEquals((long)(8 + PDU.padLengths(new int[]{"Gustav ".getBytes(StandardCharsets.UTF_8).length})),
                (long)this.pleave.toByteArray().length);
    }

    @Test
    public void getOpCode() throws Exception {
        Assert.assertEquals((long) OpCode.PLEAVE.value, (long)this.pleave.toByteArray()[0]);
    }

}