package pdu.pduTypes;

import pdu.ByteSequenceBuilder;

import java.io.IOException;
import java.io.InputStream;
import pdu.PDU;
import pdu.OpCode;

public class GETLIST  extends PDU {

    public GETLIST() {

    }

    public GETLIST (InputStream input) throws IOException {

    }

    @Override
    public byte[] toByteArray() {

        return
                new  ByteSequenceBuilder(OpCode.GETLIST.value)
                        .pad()
                        .toByteArray();
    }

    @Override
    public OpCode getOpCode(){
        return OpCode.GETLIST;
    }
}
