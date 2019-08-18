package pdu.pduTypes;

import pdu.OpCode;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by aminebalta on 2018-04-04.
 */
public class PARTICIPANTSTest {

    @Test
    public void nicknameCorr() throws Exception{

        String[] nickname = new String[]{"Amine", "Matilda", "Lovvan", "Soffan"};
        HashSet hashNick = new HashSet();
        Collections.addAll(hashNick, nickname);
        Assert.assertEquals(hashNick, (new PARTICIPANTS(nickname)).getClients());
    }

    @Test
    public void nicknameLenCorr() throws Exception {

        String[] nickname = new String[] {"Adam", "Nicklas", "JP"};
        byte[] byt = (new PARTICIPANTS(nickname)).toByteArray();
        int lengthCorr = 0;
        String [] theLength = nickname;
        int i = nickname.length;

        for(int j = 0; j < i; j++){
            String nick  = theLength[j];
            lengthCorr += nick.getBytes(StandardCharsets.UTF_8).length +1;
        }
        int k = (byt[2] << 8) + byt[3];
        Assert.assertEquals((long)lengthCorr, (long)k);
    }
    @Test
    public void corrLen () throws Exception{
        String nickname = "Matilda";
        byte[] nickByte = nickname.getBytes(StandardCharsets.UTF_8);
        PARTICIPANTS parNick = new PARTICIPANTS(new String[]{nickname});
        Assert.assertEquals(12, (long)parNick.toByteArray().length);
    }

    @Test
    public void counterParticipant() throws Exception {
        String [] nickname = new String[]{"Adam", "Nicklas", "JP"};
        byte[] PduBytes = (new PARTICIPANTS(nickname)).toByteArray();
        Assert.assertEquals((long)nickname.length, (long)PduBytes[1]);
    }

    @Test
    public void OpCode() throws Exception {
        String nickname = "Matilda";
        PARTICIPANTS ParPDU = new PARTICIPANTS(new String[]{nickname});
        Assert.assertEquals((long)OpCode.PARTICIPANTS.value,
                (long)ParPDU.toByteArray()[0]);
    }

    @Test
    public void PadZeroRequired() throws Exception {
        String [] nickname = new String[]{"Adam", "Nicklas", "JP"};
        byte[] PduBytes = (new PARTICIPANTS(nickname)).toByteArray();
        int expectedLength = 0;
        String[] i = nickname;
        int var = nickname.length;

        for(int var2 = 0; var2 < var; var2++){
            String pars = i[var2];
            expectedLength += pars.getBytes(StandardCharsets.UTF_8).length + 1;
        }

        for(int var3 = 4 + expectedLength; var3 < PduBytes.length; var3++){
            Assert.assertEquals(0L, (long)PduBytes[var3]);
        }
    }
}