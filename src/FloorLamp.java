/**
 * The FloorLamp class represents a floor lamp.
 */
public class FloorLamp {
    /** Indicates whether the lamp is on or off. */
    private boolean lampOn;

    /**
     * Constructs a new FloorLamp object with the lamp initially off.
     */
    public FloorLamp(){
        lampOn = false;
    }

    /**
     * Turns on the lamp.
     */
    public void turnOnLamp(){
        lampOn = true;
    }

    /**
     * Turns off the lamp.
     */
    public void turnOffLamp(){
        lampOn = false;
    }

    /**
     * Checks if the lamp is on.
     *
     * @return true if the lamp is on, false otherwise.
     */
    public boolean isLampOn() {
        return lampOn;
    }
}
