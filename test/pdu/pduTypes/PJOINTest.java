package pdu.pduTypes;

import jdk.nashorn.internal.runtime.ECMAException;
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
public class PJOINTest {
    private static Date time = new Date(6373898);
    private static String participant = "Måns ";
    private PJOIN pjoin;

    public PJOINTest(){
        this.pjoin = new PJOIN(participant, time);
    }
    @Test
    public void nicknameLen()throws Exception{
        Assert.assertEquals((long)"Måns ".getBytes(StandardCharsets.UTF_8).length, (long)this.pjoin.toByteArray()[1]);
    }
    @Test
    public void corrOp() throws Exception{
        Assert.assertEquals(12, (long)this.pjoin.toByteArray()[0]);
    }
    @Test
    public void corrLen () throws Exception{
        Assert.assertEquals((long)(8 + PDU.padLengths(new int[]{
                "Måns ".getBytes(StandardCharsets.UTF_8).length})), (long)this.pjoin.toByteArray().length);
    }
    @Test
    public void corrNick() throws Exception{
        byte[] pduBytes = this.pjoin.toByteArray();
        byte[] nickBytes = new byte[pduBytes[1]];
        System.arraycopy(pduBytes, 8, nickBytes, 0, nickBytes.length);
        Assert.assertEquals("Måns ", new String(nickBytes, StandardCharsets.UTF_8));
    }
}