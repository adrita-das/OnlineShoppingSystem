package test;

import org.junit.jupiter.api.Test;
import service.RegisterService;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {


    // -----------------BVA Start -----------//

    // Name below minimum: 1 character (min-1)
    @Test
    void nameBelowMinimumTest() {
        RegisterService service = new RegisterService();

        boolean result = service.validateRegistration(
                "A",
                "adrita@gmail.com",
                "Test1234",
                "Test1234"
        );

        assertFalse(result);
    }

// Name exactly minimum: 2 characters (exactly min)
    @Test
    void nameExactlyMinimumTest() {
        RegisterService service = new RegisterService();

        boolean result = service.validateRegistration(
                "Ad",
                "adrita@gmail.com",
                "Test1234",
                "Test1234"
        );

        assertTrue(result);
    }

    // Name above nominal: 4 characters
    @Test
    void nameAboveMinimumTest() {
        RegisterService service = new RegisterService();

        boolean result = service.validateRegistration(
                "Adri",
                "adrita@gmail.com",
                "Test1234",
                "Test1234"
        );

        assertTrue(result);
    }

    // Name exactly maximum: 10 characters
    @Test
    void nameExactlyMaximumTest() {
        RegisterService service = new RegisterService();

        boolean result = service.validateRegistration(
                "stqaprojec",
                "stqa@gmail.com",
                "Test1234",
                "Test1234"
        );

        assertTrue(result);
    }

    // Name above maximum: 10 characters
    @Test
    void nameAboveMaximumTest() {
        RegisterService service = new RegisterService();

        boolean result = service.validateRegistration(
                "stqaproject",
                "stqa@gmail.com",
                "Test1234",
                "Test1234"
        );

        assertTrue(result);
    }


    // Password below minimum: 7 characters
    @Test
    void passwordBelowMinimumTest() {
        RegisterService service = new RegisterService();

        boolean result = service.validateRegistration(
                "Adrita",
                "adrita@gmail.com",
                "Test123",
                "Test123"
        );

        assertFalse(result);
    }

    // Password exactly minimum: 8 characters
    @Test
    void passwordExactly8CharacterTest() {
        RegisterService service = new RegisterService();

        boolean result = service.validateRegistration(
                "Adrita",
                "adrita@gmail.com",
                "1234a567",
                "1234a567"
        );

        assertTrue(result);
    }

    // Password exactly maximum: 20 characters
    @Test
    void passwordExactly20CharacterTest() {
        RegisterService service = new RegisterService();

        boolean result = service.validateRegistration(
                "Adrita",
                "adrita@gmail.com",
                "123456789@Testtest20",
                "123456789@Testtest20"
        );

        assertTrue(result);
    }

    // Password above maximum: 21 characters
    @Test
    void passwordAboveMaximumTest() {
        RegisterService service = new RegisterService();

        boolean result = service.validateRegistration(
                "Adrita",
                "adrita@gmail.com",
                "123456789@Testtest20a",
                "123456789@Testtest20a"
        );

        assertFalse(result);
    }

    //-----BVA End --------//

    //----------------------ECT Testing start  --------------//

    //weak normal
    @Test
    void weakNormalTest() {

        RegisterService service = new RegisterService();

        boolean result = service.validateRegistration(
                "Adrita",
                "adrita@gmail.com",
                "Test1234",
                "Test1234"
        );

        assertTrue(result);
    }

    // strong normal testing

    @Test
    void strongNormalTest() {

        RegisterService service = new RegisterService();

        boolean result1 = service.validateRegistration(
                "Ad",
                "user123@gmail.com",
                "abc12345",
                "abc12345"
        );

        boolean result2 = service.validateRegistration(
                "Adrita",
                "adrita@gmail.com",
                "Test1234",
                "Test1234"
        );

        assertTrue(result1);
        assertTrue(result2);
    }

    // weak-roboust testing

    @Test
    void weakRobustInvalidEmailTest() {

        RegisterService service = new RegisterService();

        boolean result = service.validateRegistration(
                "Adrita",
                "adritagmail.com",
                "Test1234",
                "Test1234"
        );

        assertFalse(result);
    }

    @Test
    void weakRobustPasswordOnlyNumberTest() {

        RegisterService service = new RegisterService();

        boolean result = service.validateRegistration(
                "Adrita",
                "adrita@gmail.com",
                "12345678",
                "12345678"
        );

        assertFalse(result);
    }

    @Test
    void weakRobustConfirmPasswordTest() {

        RegisterService service = new RegisterService();

        boolean result = service.validateRegistration(
                "Adrita",
                "adrita@gmail.com",
                "Test1234",
                "Test5678"
        );

        assertFalse(result);
    }

    // strong roboust testing

    @Test
    void strongRobustInvalidCombinationTest() {

        RegisterService service = new RegisterService();

        boolean result = service.validateRegistration(
                "A",
                "adritagmail.com",
                "12345678",
                "87654321"
        );

        assertFalse(result);
    }

    @Test
    void strongRobustMultipleInvalidTest() {

        RegisterService service = new RegisterService();

        boolean result = service.validateRegistration(
                "Adrita",
                "",
                "Testabcd",
                "Different1"
        );

        assertFalse(result);
    }


    //----------------------ECT Testing end  --------------//

}

