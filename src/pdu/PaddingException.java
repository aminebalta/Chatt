package pdu;

/**
 * Created by Amine Balta and Matilda Nilsson  on 2018-05-23.
 */
public class PaddingException extends Exception {

    public PaddingException() throws PaddingException {
        System.err.println("Non-zero paddning");
    }

}
