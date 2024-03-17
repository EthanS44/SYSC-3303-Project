public class DirectionLamp {

    private boolean lampOn;
    private int lampDirection; //1 for up, 0 for down

    private Elevator elevator;

    public DirectionLamp(Elevator elevator){
        this.lampOn = false;
        this.lampDirection = 0;
        this.elevator = elevator;
    }


}
