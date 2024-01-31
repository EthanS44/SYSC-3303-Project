public class Elevator implements Runnable {

    private final int elevatorID;
    private boolean stopped;
    private int currentFloor;


    public Elevator(int elevatorID){
        this.elevatorID = elevatorID;
        currentFloor = 1;
    }

    public int getElevatorID() {
        return elevatorID;
    }

    public boolean isStopped(){
        return stopped;
    }

    public void buttonPushed(){
        Request request = new Request();
    }

    public void goToFloor(int newFloor){
        System.out.printf("Going to floor " + newFloor);
        
        currentFloor = newFloor;
    }


    @Override
    public void run(){}

}
