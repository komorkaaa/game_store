package game_store.app.services;

import game_store.app.dao.UserDAO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterServiceTest {

  private RegisterService createServiceWithMockDAO(UserDAO mockDao) {
    return new RegisterService(mockDao);
  }

  @Test
  void testEmptyFields() {
    UserDAO mockDao = mock(UserDAO.class);
    RegisterService service = createServiceWithMockDAO(mockDao);

    String result = service.registerUser(
            "", "", "", "",
            "", "", LocalDate.now(),
            ""
    );

    assertEquals("Заполните все поля", result);
  }

  @Test
  void testInvalidEmail() {
    UserDAO mockDao = mock(UserDAO.class);
    RegisterService service = createServiceWithMockDAO(mockDao);

    String result = service.registerUser(
            "TestUser", "invalidEmail",
            "12345", "12345",
            "123456789012", "1234567890",
            LocalDate.now().minusYears(18),
            "+7-123-123-12-12"
    );

    assertEquals("Невалидный email", result);
  }

  @Test
  void testUserAlreadyExists() {
    UserDAO mockDao = mock(UserDAO.class);

    when(mockDao.registerUser(
            anyString(), anyString(), anyString(),
            any(LocalDate.class), anyString(), anyString(), anyString()
    )).thenReturn(false);

    RegisterService service = createServiceWithMockDAO(mockDao);

    String result = service.registerUser(
            "ValidName", "test@mail.com",
            "pass", "pass",
            "123456789012", "1234567890",
            LocalDate.now().minusYears(20),
            "+7-123-123-12-12"
    );

    assertEquals("Ошибка: логин или email уже используются", result);
  }

  @Test
  void testSuccessfulRegistration() {
    UserDAO mockDao = mock(UserDAO.class);

    // ✔ Пользователь не существует, регистрация успешна
    when(mockDao.registerUser(
            anyString(), anyString(), anyString(),
            any(LocalDate.class), anyString(), anyString(), anyString()
    )).thenReturn(true);

    RegisterService service = createServiceWithMockDAO(mockDao);

    String result = service.registerUser(
            "ValidName", "test@mail.com",
            "pass", "pass",
            "123456789012", "1234567890",
            LocalDate.now().minusYears(20),
            "+7-123-123-12-12"
    );

    assertEquals("SUCCESS", result);
  }
}
