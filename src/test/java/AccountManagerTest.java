import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountManagerTest {

    // 1. Use Annotations to create mocks automatically
    @Mock
    private IUserRepository userRepository;
    @Mock
    private IPasswordEncoder passwordEncoder;
    @Mock
    private ILogger logger;

    // 2. InjectMocks automatically initializes AccountManager with the mocks above
    @InjectMocks
    private AccountManager accountManager;

    // Constants to avoid typing strings repeatedly
    private static final String VALID_USER = "validUser";
    private static final String VALID_PASS = "StrongPass1";

    // --- US-01 : SUCCESS PATH ---
    @Test
    void shouldReturnSuccessWhenCredentialsAreValidAndUserDoesNotExist() {
        // Arrange
        when(userRepository.userExists(VALID_USER)).thenReturn(false);
        when(passwordEncoder.encode(VALID_PASS)).thenReturn("HASHED");

        // Act
        RegistrationStatus result = accountManager.registerUser(VALID_USER, VALID_PASS);

        // Assert
        assertEquals(RegistrationStatus.SUCCESS, result);

        // Verify
        verify(passwordEncoder).encode(VALID_PASS);
        verify(userRepository).saveUser(VALID_USER, "HASHED");
        verify(logger).logInfo(contains(VALID_USER));
    }

    // --- US-02 : INVALID_INPUT ---
    @Test
    void shouldReturnInvalidInputWhenUsernameIsNull() {
        RegistrationStatus result = accountManager.registerUser(null, VALID_PASS);

        assertEquals(RegistrationStatus.INVALID_INPUT, result);
        verifyNoSideEffects();
    }

    @Test
    void shouldReturnInvalidInputWhenPasswordIsEmpty() {
        RegistrationStatus result = accountManager.registerUser(VALID_USER, "");

        assertEquals(RegistrationStatus.INVALID_INPUT, result);
        verifyNoSideEffects();
    }

    // --- US-03 : INVALID_FORMAT ---
    @Test
    void shouldReturnInvalidFormatWhenUsernameTooShort() {
        RegistrationStatus result = accountManager.registerUser("abc", VALID_PASS);

        assertEquals(RegistrationStatus.INVALID_FORMAT, result);
        verifyNoSideEffects();
    }

    @Test
    void shouldReturnInvalidFormatWhenPasswordTooShort() {
        RegistrationStatus result = accountManager.registerUser(VALID_USER, "Abc12");

        assertEquals(RegistrationStatus.INVALID_FORMAT, result);
        verifyNoSideEffects();
    }

    @Test
    void shouldReturnInvalidFormatWhenPasswordHasNoDigit() {
        RegistrationStatus result = accountManager.registerUser(VALID_USER, "Password");

        assertEquals(RegistrationStatus.INVALID_FORMAT, result);
        verifyNoSideEffects();
    }

    // --- US-04 : USER ALREADY EXISTS ---
    @Test
    void shouldReturnInvalidUsernameAlreadyExistsWhenUserAlreadyExists() {
        // Arrange
        when(userRepository.userExists(VALID_USER)).thenReturn(true);

        // Act
        RegistrationStatus result = accountManager.registerUser(VALID_USER, VALID_PASS);

        // Assert
        assertEquals(RegistrationStatus.INVALID_USERNAME_ALREADY_EXISTS, result);

        // Verify only the check happened, but nothing else
        verify(userRepository).userExists(VALID_USER);
        verifyNoSideEffects();
    }

    // --- HELPER METHOD ---
    // Extracting this removes 15+ lines of duplicate code
    private void verifyNoSideEffects() {
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).saveUser(any(), any());
        verify(logger, never()).logInfo(any());
    }
}