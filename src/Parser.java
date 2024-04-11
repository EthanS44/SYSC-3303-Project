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
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Parser class is responsible for parsing instructions from a text file
 * and sending corresponding requests to either elevators or floors.
 */
public class Parser {
    public DatagramSocket sendSocket;
    /**
     * Constructs a new Parser with the given socket number.
     *
     * @param socketNumber The socket number to use.
     */
    public Parser(int socketNumber){
        try {
            sendSocket = new DatagramSocket(socketNumber);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructs a new Parser with the default constructor.
     */
    public Parser(){}

    /**
     * Parses a text file containing instructions and sends corresponding requests.
     *
     * @param textfile The text file to parse.
     */
    public void parseTextFile(File textfile){
        try (BufferedReader br = new BufferedReader(new FileReader(textfile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Ignore comment lines
                if (!line.startsWith("#")) {
                    String[] instructions = line.split(" ");
                    //System.out.println(Arrays.toString(instructions));

                    boolean isElevator = Boolean.parseBoolean(instructions[0]);
                    //LocalDateTime time = LocalDateTime.parse(instructions[1], DateTimeFormatter.ofPattern("HH:mm:ss:SSS"));
                    String time = instructions[1];
                    int indexNumber = Integer.parseInt(instructions[2]);
                    int buttonId = Integer.parseInt(instructions[3]);

                    int triggerFault = 0;

                    switch(instructions[4]){
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
                        sendRequestToElevator(indexNumber, buttonId - 1, triggerFault);
                    } else {
                        sendRequestToFloor(indexNumber, buttonId, triggerFault);
                    }
                    Thread.sleep(20000);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Parses a text file containing instructions and returns an ArrayList of Instructions.
     *
     * @param textfile The text file to parse.
     * @return An ArrayList of Instructions parsed from the text file.
     */
    public ArrayList<Instruction> testParseTextFile(File textfile){
        ArrayList<Instruction> dataList = new ArrayList<Instruction>();
        Instruction data1;
        Instruction data2;
        Instruction data3;
        int index = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(textfile))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Ignore comment lines
                if (!line.startsWith("#")) {
                    String[] instructions = line.split(" ");
                    System.out.println(Arrays.toString(instructions));

                    int indexNumber = Integer.parseInt(instructions[2]);
                    int buttonID = Integer.parseInt(instructions[3]);

                    int triggerFault = 0;

                    switch (instructions[4]) {
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
                    // data is the byte array that represents the Request
                    byte[] data = new byte[2];
                    data[0] = (byte) buttonID;
                    data[1] = (byte) triggerFault;

                    boolean buttonBoolean = (buttonID == 0)? true: false;

                    // Send packet to Scheduler receive socket
                    if (index == 0) {
                        data1 = new Instruction(buttonBoolean, indexNumber, triggerFault);
                        dataList.add(data1);
                    } else if (index == 1) {
                        data2 = new Instruction(buttonBoolean, indexNumber, triggerFault);
                        dataList.add(data2);
                    } else if (index == 2) {
                        data3 = new Instruction(buttonBoolean, indexNumber, triggerFault);
                        dataList.add(data3);
                    }
                    index++;
                }

            }
            return dataList;
        } catch (IOException e) {
        e.printStackTrace();
        }
        return null;
    }
    /**
     * Sends a request to a floor with the specified parameters.
     *
     * @param floorID The ID of the floor.
     * @param direction The direction of the request.
     * @param faultType The type of fault.
     */
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
    /**
     * Sends a request to an elevator with the specified parameters.
     *
     * @param elevatorID The ID of the elevator.
     * @param buttonId The ID of the button pressed.
     * @param faultType The type of fault.
     */
    private void sendRequestToElevator(int elevatorID, int buttonId, int faultType){
        try {
            // data is the byte array that represents the Request
            byte[] data = new byte[2];
            data[0] = (byte) buttonId;
            data[1] = (byte) faultType;

            // Send packet to Scheduler receive socket
            DatagramPacket packetToSend = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 749 + elevatorID);
            sendSocket.send(packetToSend);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to send request to Scheduler");
            System.exit(1);
        }
    }
}
