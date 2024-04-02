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

                    boolean isElevator = Boolean.parseBoolean(instructions[0]);
                    LocalDateTime time = LocalDateTime.parse(instructions[1], DateTimeFormatter.ofPattern("HH:mm:ss:SSS"));
                    int indexNumber = Integer.parseInt(instructions[2]);
                    int buttonId = Integer.parseInt(instructions[3]);
                    int currentFloor = Integer.parseInt(instructions[4]);
                    int elevatorFloorId = Integer.parseInt(instructions[5]);
                    String triggerFault = instructions[6];

                    sendRequestToScheduler (new Request(isElevator, time, indexNumber, buttonId, currentFloor, elevatorFloorId, triggerFault));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void sendRequestToScheduler(Request requestToSend){
        try {
            // data is the byte array that represents the Request
            byte[] data = requestToSend.toByteArray(requestToSend);

            // Send packet to Scheduler receive socket
            DatagramPacket packetToSend = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 41);
            sendSocket.send(packetToSend);

            if (requestToSend.isElevator()){
                System.out.println("Elevator " + requestToSend.getIndexNumber() + " Sent Request from Button " + requestToSend.getButtonId());
            } else {
                System.out.println("Floor " + requestToSend.getIndexNumber() + "Sent Request");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to send request to Scheduler");
            System.exit(1);
        }
    }
}
