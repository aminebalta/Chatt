package pdu.pduTypes;

import org.junit.Assert;
import org.junit.Test;
import pdu.OpCode;

import static org.junit.Assert.*;

/**
 * Created by aminebalta on 2018-02-06.
 */
public class GETLISTTest {

    private final GETLIST getList = new GETLIST();

    public GETLISTTest() {}

    @Test
    public void OpCodeRight() throws Exception{
        Assert.assertEquals(OpCode.GETLIST.value, getList.toByteArray()[0]);
    }

    @Test
    public void padWord() throws Exception {

        Assert.assertEquals(0L, (long)this.getList.toByteArray()[1]);
        Assert.assertEquals(0L, (long)this.getList.toByteArray()[2]);
        Assert.assertEquals(0L, (long)this.getList.toByteArray()[3]);
    }

}