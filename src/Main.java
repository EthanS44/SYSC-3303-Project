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
        Thread ninthFloor = new Thread(new Floor(9), "Ninth Floor");
        Thread tenthFloor = new Thread(new Floor(10), "Tenth Floor");
        Thread eleventhFloor = new Thread(new Floor(11), "Eleventh Floor");
        Thread twelfthFloor = new Thread(new Floor(12), "Twelfth Floor");
        Thread thirteenthFloor = new Thread(new Floor(13), "Thirteenth Floor");
        Thread fourteenthFloor = new Thread(new Floor(14), "Fourteenth Floor");
        Thread fifteenthFloor = new Thread(new Floor(15), "Fifteenth Floor");
        Thread sixteenthFloor = new Thread(new Floor(16), "Sixteenth Floor");
        Thread seventeenthFloor = new Thread(new Floor(17), "Seventeenth Floor");
        Thread eighteenthFloor = new Thread(new Floor(18), "Eighteenth Floor");
        Thread nineteenthFloor = new Thread(new Floor(19), "Nineteenth Floor");
        Thread twentiethFloor = new Thread(new Floor(20), "Twentieth Floor");
        Thread twentyFirstFloor = new Thread(new Floor(21), "Twenty-First Floor");
        Thread twentySecondFloor = new Thread(new Floor(22), "Twenty-Second Floor");

        //create elevator thread
        Thread elevatorThread1 = new Thread(new Elevator(1, 30, 50, 60), "Elevator 1");
        Thread elevatorThread2 = new Thread(new Elevator(2, 31, 51, 61), "Elevator 2");
        Thread elevatorThread3 = new Thread(new Elevator(3, 32, 52, 62), "Elevator 3");
        //Thread elevatorThread4 = new Thread(new Elevator(4, 33, 53, 63), "Elevator 4");

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
        ninthFloor.start();
        tenthFloor.start();
        eleventhFloor.start();
        twelfthFloor.start();
        thirteenthFloor.start();
        fourteenthFloor.start();
        fifteenthFloor.start();
        sixteenthFloor.start();
        seventeenthFloor.start();
        eighteenthFloor.start();
        nineteenthFloor.start();
        twentiethFloor.start();
        twentyFirstFloor.start();
        twentySecondFloor.start();


        elevatorThread1.start();
        elevatorThread2.start();
        elevatorThread3.start();
        //elevatorThread4.start();

    }
}