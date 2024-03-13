// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        // create an elevator queue object
        ElevatorQueue elevatorQueue = new ElevatorQueue();

        //create scheduler thread
        Thread schedulerThread = new Thread(new Scheduler(), "Elevator Scheduler");


        //create floor threads
        Thread firstFloor = new Thread(new Floor(1), "First Floor");
        Thread secondFloor = new Thread(new Floor(2), "Second Floor");
        Thread thirdFloor = new Thread(new Floor(3), "Third Floor");
        Thread fourthFloor = new Thread(new Floor(4), "Fourth Floor");
        Thread fifthFloor = new Thread(new Floor(5), "Fifth Floor");
        Thread sixthFloor = new Thread(new Floor(6), "Sixth Floor");
        Thread seventhFloor = new Thread(new Floor(7), "Seventh Floor");
        Thread eighthFloor = new Thread(new Floor(8), "Eighth Floor");

        //create elevator thread
        Thread elevatorThread = new Thread(new Elevator(1, 30, 50), "Elevator");
        Thread elevatorThread2 = new Thread(new Elevator(2, 31, 51), "Elevator 2");

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
        elevatorThread2.start();
    }
}