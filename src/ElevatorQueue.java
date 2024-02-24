import java.util.ArrayList;

public class ElevatorQueue {
    private ArrayList<Request> requestBox;
    private ArrayList<Instruction> instructionBox;
    private ArrayList<Response> responseBox;
    private boolean requestBoxEmpty = true;
    private boolean instructionBoxEmpty = true;
    private boolean responseBoxEmpty = true;

    private static final int numberOfFloors = 7; // idk if we need this or not yet

    public ElevatorQueue(){
        requestBox = new ArrayList<Request>();
        instructionBox = new ArrayList<Instruction>();
        responseBox = new ArrayList<Response>();
    }
    /**
     * This method Checks if the request box is empty
     * @return boolean
     */
    public boolean isRequestBoxEmpty(){
        return requestBox.isEmpty();
    }
    /**
     * This method Checks if the Instruction box is empty
     * @return boolean
     */
    public boolean isInstructionBoxEmpty(){
        return instructionBox.isEmpty();
    }

    /**
     * this method checks if response box is empty
     * @return boolean
     */
    public boolean isResponseBoxEmpty(){
        return responseBox.isEmpty();
    }

    /**
     * This method returns the instruction box
     * @return ArrayList<Instruction>
     */
    public ArrayList<Instruction> instructionBox(){
        return instructionBox;
    }

    /**
     * puts a request into the request box
     * @param request - the request to be put in
     */
    public synchronized void putInRequestBox(Request request){ // Sam Wilson 101195493
        // Does not need a check as requestBox is never full
        requestBox.add(request);
        requestBoxEmpty = false;
        //System.out.println("Request put in box, current size: " + requestBox.size() + "\n");
        notifyAll();
    }

    /**
     * Gets a request from the box
     * @return - the request in the 0th position in the list
     */
    public synchronized Request getFromRequestBox() { // Sam Wilson 101195493
        // Wait for requestBox to be not empty
        while (requestBox.isEmpty()) {
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
        System.out.println("Request taken from box by scheduler, current size: " + requestBox.size() + "\n");
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
        System.out.println("Instruction put in box, current size: " + instructionBox.size() + "\n");
    }

    /**
     * Gets the instruction from the instruction box
     * @return - The instruction at the 0th position in the list
     */
    public synchronized Instruction getFromInstructionBox(){
        // wait for instructionBox to be not empty
        while(instructionBox.isEmpty()){
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
        System.out.println("Instruction taken from box, current size: " + instructionBox.size() + "\n");

        return instruction;
    }

    /**
     * puts a response into the responseBox
     * @param response - The response to add to the Box
     */
    public synchronized void putInResponseBox(Response response){ // Sam Wilson 101195493
        // Does not need a check as responseBox is never full
        responseBox.add(response);
        responseBoxEmpty = false;
        System.out.println("Response put in box, current size: " + responseBox.size() + "\n");
    }

    /**
     * Gets a response from the responseBox
     * @return - the response in the 0th position in the list
     */
    public synchronized Response getFromResponseBox() { // Sam Wilson 101195493
        // Wait for requestBox to be not empty
        while (responseBox.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        // get first request in responseBox
        Response response = responseBox.get(0);
        // remove request from responseBox
        responseBox.remove(response);

        // set responseBoxEmpty to true if requestBox is empty
        if (responseBox.isEmpty()) {
            responseBoxEmpty = true;
        }
        System.out.println("Response taken from box, current size: " + responseBox.size() + "\n");
        notifyAll();
        return response;
    }

    public ArrayList<Response> responseBox(){
        return responseBox;
    }
}
