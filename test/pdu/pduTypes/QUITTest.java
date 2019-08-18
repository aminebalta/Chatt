package pdu.pduTypes;

import org.junit.Assert;
import org.junit.Test;
import pdu.OpCode;
import pdu.pduTypes.QUIT;

import static org.junit.Assert.*;

/**
 * Created by aminebalta on 2017-12-20.
 */
public class QUITTest {

    private final QUIT quit = new QUIT();

    public QUITTest(){}

    @Test

    public void OpCodRight () throws Exception{
        Assert.assertEquals((long)OpCode.QUIT.value, (long)this.quit.toByteArray()[0]);
    }

    @Test
    public void padWord() throws Exception {

        Assert.assertEquals(0L, (long)this.quit.toByteArray()[1]);
        Assert.assertEquals(0L, (long)this.quit.toByteArray()[2]);
        Assert.assertEquals(0L, (long)this.quit.toByteArray()[3]);
    }

}