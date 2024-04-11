import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RequestTest {
    /**
     * Test request getters
     */
    @Test
    public void testRequestGetters() {
        // Test creating a request from an elevator button press
        LocalDateTime currentTime = LocalDateTime.now();
        Request elevatorRequest = new Request(true, currentTime, 1, 4, 5, 5, 0);
        assertTrue(elevatorRequest.isElevator());
        assertEquals(currentTime, elevatorRequest.getTime());
        assertEquals(4, elevatorRequest.getButtonId());
        assertEquals(5, elevatorRequest.getCurrentFloor());


        // Test creating a request from a floor button press
        Request floorRequest = new Request(false, currentTime, 2, 1, 3, 3, 0);
        assertFalse(floorRequest.isElevator());
        assertEquals(currentTime, floorRequest.getTime());
        assertEquals(2, floorRequest.getIndexNumber());
        assertEquals(1, floorRequest.getButtonId());
        assertEquals(3, floorRequest.getCurrentFloor());

    }
    /**
     * Test request serialization
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        LocalDateTime currentTime = LocalDateTime.now();
        Request request = new Request(false, currentTime, 1, 1, 1, 1, 0);
        Request request2 = null;
        try {
            byte[] bytes = Request.toByteArray(request);
            request2 = Request.toRequest(bytes);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Exception");
        }

        assertEquals(request2.getButtonId(), request.getButtonId());
        assertEquals(request2.getIndexNumber(), request.getIndexNumber());
    }
}