import java.util.ArrayList;

public class ElevatorQueue {
    private ArrayList<Request> requestBox;
    private ArrayList<Instruction> instructionBox; // could be changed from 'Object' in the future
    private boolean requestBoxEmpty = true;
    private boolean instructionBoxEmpty = true;

    private static final int numberOfFloors = 7; // idk if we need this or not yet

    public ElevatorQueue(){
        requestBox = new ArrayList<Request>();
        instructionBox = new ArrayList<Instruction>();
    }

    public synchronized void putInRequestBox(Request request){ // Sam Wilson 101195493
        // Does not need a check as requestBox is never full
        requestBox.add(request);
        requestBoxEmpty = false;
        System.out.println("Request put in box\n");
        notifyAll();
    }

    public synchronized Request getFromRequestBox() { // Sam Wilson 101195493
        // Wait for requestBox to be not empty
        while (requestBoxEmpty) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        // get first request in requestBox
        Request request = requestBox.get(0);
        // remove request from requestBox
        requestBox.remove(request);

        // set requestBoxEmpty to true if requestBox is empty
        if (requestBox.isEmpty()) {
            requestBoxEmpty = true;
        }
        System.out.println("Request taken from box by scheduler\n");
        notifyAll();
        return request;
    }

    public synchronized void putInInstructionBox(Instruction instruction) {
        instructionBox.add(instruction);
        instructionBoxEmpty = false;
        notifyAll();
    }

    public synchronized Instruction getFromInstructionBox(){
        // wait for instructionBox to be not empty
        while(instructionBoxEmpty){
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        // get first instruction from instructionBox
        Instruction instruction = instructionBox.get(0);
        // remove instruction from instructionBox
        instructionBox.remove(instruction);

        // set instructionBoxEmpty to true if instructionBox is empty
        if (instructionBox.isEmpty()){
            instructionBoxEmpty = true;
        }
        notifyAll();

        return instruction;
    }
}
