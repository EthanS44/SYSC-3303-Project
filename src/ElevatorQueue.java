import java.util.ArrayList;

public class ElevatorQueue {
    private ArrayList<Request> requestBox;
    private ArrayList<Instruction> instructionBox; // could be changed from 'Object' in the future
    private boolean requestBoxEmpty = true;
    private boolean instructionBoxEmpty = true;

    private static final int numberOfFloors = 7; // idk if we need this or not yet

    /**
     * Constructor for the Elevator that takes initializes a requestBox and instructionBox
     */
    public ElevatorQueue(){
        requestBox = new ArrayList<Request>();
        instructionBox = new ArrayList<Instruction>();
    }

    /**
     * puts a request into the request box
     * @param request - the request to be put in
     */
    public synchronized void putInRequestBox(Request request){ // Sam Wilson 101195493
        // Does not need a check as requestBox is never full
        requestBox.add(request);
        requestBoxEmpty = false;
        System.out.println("Request put in box\n");
        notifyAll();
    }

    /**
     * Gets a request from the box
     * @return - the request in the 0th position in the list
     */
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

    /**
     * puts an instruction into the instructionBox
     * @param instruction - the instruction to be put into the box
     */
    public synchronized void putInInstructionBox(Instruction instruction) {
        instructionBox.add(instruction);
        instructionBoxEmpty = false;
        notifyAll();
    }

    /**
     * Gets the instruction from the instruction box
     * @return - The instruction at the 0th position in the list
     */
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
