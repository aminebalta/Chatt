package pdu.pduTypes;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.TIMEOUT;
import pdu.Checksum;
import pdu.OpCode;
import pdu.PDU;

import java.nio.charset.StandardCharsets;
import java.security.Timestamp;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by aminebalta on 2018-02-06.
 */

//TODO

public class MESSTest {

    private static final String NAME = "Amine ";
    private static final String MESSAGE = "Korv är gott ";
    private static final MESS client = new MESS("Korv är gott ");
    private static final Date DATE = new Date();
    private MESS server;
    private static final byte[] niBytes = NAME.getBytes(StandardCharsets.UTF_8);
    private static final byte[] meBytes = MESSAGE.getBytes(StandardCharsets.UTF_8);
    private static final byte[] cliBytes = client.toByteArray();
    private byte[] serBytes;

    public MESSTest(){}

    @Before
    public void messSetup() {
        server = new MESS(MESSAGE);
        serBytes = server.toByteArray();
    }

    @Test
    public void rightClientOp () throws Exception {
        Assert.assertEquals((long)OpCode.MESS.value, (long)cliBytes[0]);
    }

    @Test
    public void rightServerOp () throws Exception {
        Assert.assertEquals((long) OpCode.MESS.value, (long)serBytes[0]);
    }
    @Test
    public void serverLength() throws Exception {
        Assert.assertEquals((long)PDU.padLengths(28), (long)serBytes.length);
    }

    @Test
    public void clientLength() throws Exception {
        Assert.assertEquals(28, (long)cliBytes.length);
    }

    //OL means the number zero of type long
    @Test
    public void lengthNicknameZeroClient() throws Exception {
        Assert.assertEquals(0L, (long)cliBytes[2]);
    }
    @Test
    public void timestampCorrect() throws Exception {
        Date dateCode = new Date((DATE.getTime()/1000) * 1000);
        Date realDate = new Date(PDU.byteArrayToLong(serBytes, 8, 12) * 1000);
        Assert.assertEquals(dateCode, realDate);
    }
    @Test
    public void rightPaddClient() throws Exception{
        Assert.assertEquals(0L, (long)cliBytes[6]);
        Assert.assertEquals(0L, (long)cliBytes[7]);
    }
    @Test
    public void lengthNickname() throws Exception{
        Assert.assertEquals(0L, (long)serBytes[2]);
    }
    @Test
    public void messLen() throws Exception{
        short messLen = (short)(((cliBytes[4] & 255) << 8) + (cliBytes[5] & 255));
        Assert.assertEquals((long)meBytes.length, (long)messLen);
    }
    @Test
    public void paddServer() throws Exception {
        Assert.assertEquals(0L, (long)cliBytes[6]);
        Assert.assertEquals(0L, (long)cliBytes[7]);
    }
    @Test
    public void paddServerNick() throws Exception{
        int messSt = PDU.padLengths(new int[]{12, meBytes.length});
        int paddSt = messSt + cliBytes.length;
        int totalLen = messSt + PDU.padLengths(new int[]{cliBytes.length});

        for (int i = paddSt; i < totalLen; ++i){
            Assert.assertEquals(0L, (long)serBytes[i]);
        }
    }
    @Test
    public void messLenServer() throws Exception{
        short messLen = (short)(((serBytes[4] & 255) << 8) + (serBytes[5] & 255));
        Assert.assertEquals((long)meBytes.length, (long)messLen);
    }
    @Test
    public void paddServerMess() throws Exception {
        for (int i = 12 + meBytes.length; i < PDU.padLengths(new int[]{12, meBytes.length}); ++i){
            Assert.assertEquals(0L, (long)serBytes[i]);
        }
    }
    @Test
    public void paddClientMess() throws Exception{
        for (int i = 12 + meBytes.length; i < PDU.padLengths(new int[]{12,
        meBytes.length}); ++i){
            Assert.assertEquals(0L, (long)cliBytes[i]);
        }
    }
    @Test
    public void checksumServer() throws Exception{
        Assert.assertEquals(0L, (long) Checksum.computeChecksum(serBytes));
    }
    @Test
    public void checksumClient() throws Exception{
        Assert.assertEquals(0L, (long)Checksum.computeChecksum(cliBytes));
    }
}