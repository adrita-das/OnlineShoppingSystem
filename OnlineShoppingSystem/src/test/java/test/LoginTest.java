package test;

import database.UserDAO;
import org.junit.jupiter.api.Test;
import service.LoginService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginTest {

    @Test
    void validLoginTest() {

        String email = "adrita@gmail.com";
        String password = "Test1234";

        UserDAO dao = mock(UserDAO.class);

        when(dao.loginUser(email, password))
                .thenReturn(true);

        LoginService service = new LoginService(dao);

        boolean result =
                service.validateLogin(email, password);

        assertTrue(result);

        verify(dao).loginUser(email, password);
    }


    @Test
    void invalidLoginTest() {

        String email = "adrita02@gmail.com";
        String password = "12345678a";

        UserDAO dao = mock(UserDAO.class);

        when(dao.loginUser(email, password))
                .thenReturn(false);

        LoginService service = new LoginService(dao);

        boolean result =
                service.validateLogin(email, password);

        assertFalse(result);

        verify(dao).loginUser(email, password);
    }
}