// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        // create an elevator queue object
        ElevatorQueue elevatorQueue = new ElevatorQueue();

        //create scheduler thread
        Thread schedulerThread = new Thread(new Scheduler(elevatorQueue), "Elevator Scheduler");


        //create floor threads
        Thread firstFloor = new Thread(new Floor(1, elevatorQueue), "First Floor");
        Thread secondFloor = new Thread(new Floor(2, elevatorQueue), "Second Floor");
        Thread thirdFloor = new Thread(new Floor(3, elevatorQueue), "Third Floor");
        Thread fourthFloor = new Thread(new Floor(4, elevatorQueue), "Fourth Floor");
        Thread fifthFloor = new Thread(new Floor(5, elevatorQueue), "Fifth Floor");
        Thread sixthFloor = new Thread(new Floor(6, elevatorQueue), "Sixth Floor");
        Thread seventhFloor = new Thread(new Floor(7, elevatorQueue), "Seventh Floor");
        Thread eighthFloor = new Thread(new Floor(8, elevatorQueue), "Eighth Floor");

        //create elevator thread
        Thread elevatorThread = new Thread(new Elevator(1, elevatorQueue), "Elevator");

        //start threads
        schedulerThread.start();

        firstFloor.start();
        secondFloor.start();
        thirdFloor.start();
        fourthFloor.start();
        fifthFloor.start();
        sixthFloor.start();
        seventhFloor.start();
        eighthFloor.start();

        elevatorThread.start();

    }
}