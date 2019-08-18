package pdu;

/**
 * Created by Amine Balta and Matilda Nilsson  on 2017-12-11.
 */
public enum OpCode {


        REG(0),
        ACK(1),
        ALIVE(2),
        NOTREG(100),
        GETLIST(3),
        SLIST(4),
        MESS(10),
        QUIT(11),
        JOIN(12),
        PJOIN(16),
        PLEAVE(17),
        PARTICIPANTS(19);

public final byte value;

        OpCode(int value) {
        this.value = (byte) value;
        }
        public byte getByte() {
        return value;
        }
}
