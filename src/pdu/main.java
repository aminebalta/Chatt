package pdu;

import pdu.pduTypes.*;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Amine Balta and Matilda Nilsson, lionel richie on 2016-10-27.
 */
public class main {
    public static void main(String[] args) throws IOException {

        /* If args do not match, system quits */
        if (args.length < 4) {
            System.err.println("Missing args");
            System.err.println("Usage: java ChatClient id {ns,cs} hostadress hostport");
            System.exit(-1);
        }
        /* Varible for args [3], which is port */
        int port = Integer.parseInt(args[3]);

        /*Sets*/
        PDUSocket selectedServer = null;
        switch (args[1]) {
            case "cs":
               selectedServer  = new PDUSocket(args[2], port);
                break;
            case "ns":
                selectedServer = chooseChatFromNameServer(args[2], port);
                break;
            default:
                System.err.println(args[1] + " is not a valid option");
                System.exit(-1);
        }

            Scanner nicknameScanner = new Scanner(System.in);
            selectedServer.writePDU(new JOIN(args[0]));

        PDUSocket finalSelectedServer = selectedServer;
        AtomicBoolean shouldRun = new AtomicBoolean(true);
        Thread inputRead = new Thread(
                    () -> {
                        while(shouldRun.get())

                            {
                                try {
                                    System.out.println("Write something : ");
                                    String mess = nicknameScanner.nextLine();
                                    if ("quit".equals(mess.trim())){
                                        finalSelectedServer.writePDU(new QUIT());
                                        System.exit(0);
                                    }
                                    finalSelectedServer.writePDU(new MESS(mess.trim()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
            );

            while (shouldRun.get()){
                PDU pdu;
                try {
                    pdu = selectedServer.readPDU();

                    switch (pdu.getOpCode()) {
                        case JOIN:
                        case PJOIN:
                            System.out.println(((PJOIN) pdu).getTimestamp() +": " + ((PJOIN) pdu).getNickname() + " " +
                                    "joined the chat");
                            break;
                        case PLEAVE:
                            System.out.println(((PLEAVE) pdu).getTimestamp()+": " + ((PLEAVE) pdu).getNickname() + " " +
                                    "left the chat" );
                            break;
                        case PARTICIPANTS:
                            System.out.println("Connected users: ");
                            Collection<String> clients = ((PARTICIPANTS) pdu).getClients();
                            for (String client : clients) {
                                System.out.println(client);
                            }
                            inputRead.start();
                            break;
                        case QUIT:
                            System.out.println("Got quit from server");
                            System.exit(0);
                            break;
                        case MESS:
                            System.out.println(((MESS) pdu).getDate() + ": " + ((MESS) pdu).getMess());
                        default:
                    }

                } catch (ChecksumException e) {
                    System.err.println("Wrong checksum");
                    selectedServer.writePDU(new QUIT());
                    shouldRun.set(false);
                } catch (PaddingException e) {
                    e.printStackTrace();
                }
            }
        }

    private static PDUSocket chooseChatFromNameServer(String address, int port) throws IOException {

        PDUSocket socket = new PDUSocket(address, port);

        socket.writePDU(new GETLIST());

        SLIST slist = null;
        try {
            slist = (SLIST) socket.readPDU();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (PaddingException e) {
            System.err.println("Got non-zero padding, disconnecting from server");
            socket.writePDU(new QUIT());
            System.exit(-1);
        }
        SLIST.ServerEntry[] entries = slist.getEntries();
        System.out.println("Found: " + entries.length + " servers");
        if(entries.length == 0){
            System.exit(-1);
        }

        Scanner s = new Scanner(System.in);
        boolean isValidServer = false;

        PDUSocket chosenServer = null;

        do {
            System.out.println("Choose server :");
            for (int i= 0; i < entries.length; i++){
                System.out.println(i+1 + ": "+ entries[i]);
            }
            int i = s.nextInt();

            isValidServer = i > 0 && i <= entries.length;
            if (isValidServer)
                chosenServer = new PDUSocket(entries[i - 1].address.getHostAddress(), entries[i - 1].port);
            else{
                System.err.println("Selected server does not exist");

            }
        }while (!isValidServer);

        return chosenServer;
    }
}
