package test;

import org.junit.jupiter.api.Test;
import service.LoginService;

import static org.junit.jupiter.api.Assertions.*;


public class LoginTest {


    @Test
    void validLoginTest(){


        LoginService service = new LoginService();


        boolean result =
                service.validateLogin(
                        "rahim2@gmail.com",
                        "12345"
                );


        assertTrue(result);

    }



    @Test
    void invalidPasswordTest(){


        LoginService service = new LoginService();


        boolean result =
                service.validateLogin(
                        "rahim2@gmail.com",
                        "wrong123"
                );


        assertFalse(result);

    }



    @Test
    void emptyEmailTest(){


        LoginService service = new LoginService();


        boolean result =
                service.validateLogin(
                        "",
                        "12345"
                );


        assertFalse(result);

    }

}