import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Parser {
    public DatagramSocket sendSocket;
    public Parser(int socketNumber){
        try {
            sendSocket = new DatagramSocket(socketNumber);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public void parseTextFile(File textfile){
        try (BufferedReader br = new BufferedReader(new FileReader(textfile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Ignore comment lines
                if (!line.startsWith("#")) {
                    String[] instructions = line.split(" ");
                    System.out.println(Arrays.toString(instructions));

                    boolean isElevator = Boolean.parseBoolean(instructions[0]);
                    //LocalDateTime time = LocalDateTime.parse(instructions[1], DateTimeFormatter.ofPattern("HH:mm:ss:SSS"));
                    String time = instructions[1];
                    int indexNumber = Integer.parseInt(instructions[2]);
                    int buttonId = Integer.parseInt(instructions[3]);
                    int currentFloor = Integer.parseInt(instructions[4]);
                    int elevatorFloorId = Integer.parseInt(instructions[5]);
                    int triggerFault = 0;

                    switch(instructions[6]){
                        case "none":
                            triggerFault = 0;
                            break;
                        case "transient":
                            triggerFault = 1;
                            break;
                        case "hard":
                            triggerFault = 2;
                            break;
                    }

                    if (isElevator){
                        sendRequestToElevator(indexNumber, buttonId, triggerFault);
                    } else {
                        sendRequestToFloor(indexNumber, buttonId, triggerFault);
                    }
                }
                Thread.sleep(15000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
    private void sendRequestToFloor(int floorID, int direction, int faultType){
        try {
            // data is the byte array that represents the Request
            byte[] data = new byte[2];
            data[0] = (byte) direction;
            data[1] = (byte) faultType;

            // Send packet to Scheduler receive socket
            DatagramPacket packetToSend = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), floorID + 100);
            sendSocket.send(packetToSend);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to send request to Scheduler");
            System.exit(1);
        }
    }
    private void sendRequestToElevator(int elevatorID, int buttonId, int faultType){
        try {
            // data is the byte array that represents the Request
            byte[] data = new byte[2];
            data[0] = (byte) buttonId;
            data[1] = (byte) faultType;

            // Send packet to Scheduler receive socket
            DatagramPacket packetToSend = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 69 + elevatorID);
            sendSocket.send(packetToSend);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to send request to Scheduler");
            System.exit(1);
        }
    }
}
