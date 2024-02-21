import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RequestTest {
    @Test
    public void testRequestGetters() {
        // Test creating a request from an elevator button press
        LocalDateTime currentTime = LocalDateTime.now();
        Request elevatorRequest = new Request(true, currentTime, 1, 4, 5);
        assertTrue(elevatorRequest.isElevator());
        assertEquals(currentTime, elevatorRequest.getTime());
        assertEquals(4, elevatorRequest.getButtonId());
        assertEquals(5, elevatorRequest.getCurrentFloor());


        // Test creating a request from a floor button press
        Request floorRequest = new Request(false, currentTime, 2, 1, 3);
        assertFalse(floorRequest.isElevator());
        assertEquals(currentTime, floorRequest.getTime());
        assertEquals(2, floorRequest.getIndexNumber());
        assertEquals(1, floorRequest.getButtonId());
        assertEquals(3, floorRequest.getCurrentFloor());

    }

    @Test
    public void testSetAndGetAcknowledge(){
        LocalDateTime currentTime = LocalDateTime.now();
        Request request = new Request(true, currentTime, 1, 1, 3);

        assertFalse(request.getRequestAcknowledged()); // Initial state is false
        request.setRequestAcknowledged(true);
        assertTrue(request.getRequestAcknowledged());
    }
}