package pdu.pduTypes;

import org.junit.Assert;
import org.junit.Test;
import pdu.OpCode;
import pdu.PDU;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Created by aminebalta on 2018-05-14.
 */
public class SLISTTest {

    private SLIST slist;
    private SLIST.ServerEntry[] entries;
    private byte[] pduBytes;

    @Test
    public void getEntries() throws Exception {
        byte[] slistCorr = new byte[]{0, 0, 1, 0,0,0,0,0,1,1,1,'a',0,0,0};
        SLIST spdu = new SLIST(new ByteArrayInputStream(slistCorr));

    }

    @Test
    public void getOpCode() throws Exception {
        byte[] slistCorr = new byte[]{0, 0, 1, 0,0,0,0,0,1,1,1,'a',0,0,0};
        SLIST spdu = new SLIST(new ByteArrayInputStream(slistCorr));
        Assert.assertEquals((long) OpCode.SLIST.value, (long) spdu.toByteArray()[0]);
    }

    @Test
    public void corrLen() throws Exception {
        byte [] slistCorr = new byte[]{0, 0, 1, 0,0,0,0,0,1,1,1,'a',0,0,0};
        SLIST spdu = new SLIST(new ByteArrayInputStream(slistCorr));
        int len = 4;
        SLIST.ServerEntry[] i = spdu.getEntries();
        int j = i.length;

        for (int k = 0; k < j; k++){
            SLIST.ServerEntry ent = i[k];
            len += 8;
            len += PDU.padLengths(new int[]{
                    ent.serverName.getBytes(StandardCharsets.UTF_8).length
            });
        }
        Assert.assertEquals((long)len, (long) spdu.toByteArray().length);
    }

    @Test
    public void getServerCount() throws Exception {
        byte [] slistCorr = new byte[]{0, 0, 1, 0,0,0,0,0,1,1,1,'a',0,0,0};
        SLIST spdu = new SLIST(new ByteArrayInputStream(slistCorr));
        Assert.assertEquals((long) spdu.getEntries().length,
                PDU.byteArrayToLong(spdu.toByteArray(), 2, 4));
    }

    @Test
    public void toString1() throws Exception {

    }

}