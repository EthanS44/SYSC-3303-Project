import java.lang.reflect.Array;
import java.util.ArrayList;

public class Scheduler implements Runnable {
    private static final int numberOfFloors = 7;
    private ArrayList<Request> requestBox;
    private ArrayList<Floor> floorsToVisit;
    private boolean requestBoxEmpty = true;
    private boolean floorsToVisitEmpty = true;

    public Scheduler(){
        requestBox = new ArrayList<Request>();
        floorsToVisit = new ArrayList<Floor>();
    }

    public void putInRequestBox(Request request){ // Sam Wilson 101195493
        // Does not need a check as requestBox is never full
        requestBox.add(request);
        requestBoxEmpty = false;
    }

    public Request getFromRequestBox() { // Sam Wilson 101195493
        // Wait for requestBox to be not empty
        while(requestBoxEmpty){
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
        if (requestBox.isEmpty()){
            requestBoxEmpty = true;
        }

        return request;
    }

    public void acknowledgeRequest(Request request){
        request.setRequestAcknowledged(true);
    }

    public void handleRequest(Request request){
        // Take request
        // figure out the floor to go to
        // add floor to list of floors to visit
        // figure out which elevator to send the instruction to
        // call sendInstruction()
    }

    public void sendInstruction(/*which elevator to send to*/){

    }

    @Override
    public void run(){
        while(true){
            Request requestToHandle = requestBox.get(0);
            acknowledgeRequest(requestToHandle);
            handleRequest(requestToHandle);
            sendInstruction(/*Elevator number*/);
        }
    }
}
