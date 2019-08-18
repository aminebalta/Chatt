package pdu.pduTypes;

import pdu.ByteSequenceBuilder;
import pdu.PDU;
import pdu.OpCode;
import pdu.PaddingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class QUIT extends PDU {

    public QUIT(){}
    public QUIT(InputStream is) throws IOException, PaddingException {
       //Kolla padding
        checkPaddning(is, 3);
    }

    @
    Override
    public byte[] toByteArray() {
        return new
                ByteSequenceBuilder(OpCode.QUIT.value).pad().toByteArray();
    }
    @Override
    public boolean equals(Object o){
        return  (o instanceof QUIT);

    }
    @Override
    public OpCode getOpCode(){
        return OpCode.QUIT;
    }
}
