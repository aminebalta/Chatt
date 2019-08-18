package pdu.pduTypes;

import org.junit.Assert;
import org.junit.Test;
import pdu.OpCode;
import pdu.PDU;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Created by aminebalta on 2018-02-06.
 */
public class JOINTest {

    private static final String NAME = "Amine ";
    private final JOIN join = new JOIN("Amine ");

    public JOINTest(){}

    @Test
    public void OpCodeRight() throws Exception{
        Assert.assertEquals(OpCode.JOIN.value, join.toByteArray()[0]);
    }

    @Test
    public void rightParLength() throws Exception{
        Assert.assertEquals((long)NAME.getBytes(StandardCharsets.UTF_8).length, (long)this.join.toByteArray()[1]);
    }

    @Test
    public void rightLength() throws Exception {
        byte[] pBytes = NAME.getBytes(StandardCharsets.UTF_8);
        Assert.assertEquals((long) PDU.padLengths(new int[]{
                4, pBytes.length
        }), (long)this.join.toByteArray().length);
    }

    @Test
    public void firstWord() throws Exception{
        byte[] pduBytes = this.join.toByteArray();
        Assert.assertEquals(0L, (long)pduBytes[2]);
        Assert.assertEquals(0L, (long)pduBytes[3]);
    }

    @Test
    public void zeros(){
        byte[] pBytes = NAME.getBytes(StandardCharsets.UTF_8);
        for (int i=4 + pBytes.length; i < this.join.toByteArray().length; ++i){
            Assert.assertEquals(0L, (long)this.join.toByteArray()[i]);
        }
    }

}