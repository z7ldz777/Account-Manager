    import org.junit.jupiter.api.Test;
    import static org.junit.jupiter.api.Assertions.*;
    import static org.mockito.Mockito.*;

    public class AccountManagerTest {

        private final IUserRepository userRepository = mock(IUserRepository.class);
        private final IPasswordEncoder passwordEncoder = mock(IPasswordEncoder.class);
        private final ILogger logger = mock(ILogger.class);

        private final AccountManager accountManager =
                new AccountManager(userRepository, passwordEncoder, logger);

        // US-01 : SUCCESS PATH
        @Test
        void shouldReturnSuccessWhenCredentialsAreValidAndUserDoesNotExist() {

            String username = "validUser";
            String password = "StrongPass1";

            when(userRepository.userExists(username)).thenReturn(false);
            when(passwordEncoder.encode(password)).thenReturn("HASHED");


            RegistrationStatus result = accountManager.registerUser(username, password);


            assertEquals(RegistrationStatus.SUCCESS, result);

            verify(passwordEncoder, times(1)).encode(password);
            verify(userRepository, times(1)).saveUser(username, "HASHED");
            // logger should be called ONCE with a message containing the username
            verify(logger, times(1)).logInfo(contains(username));
        }

        // US-02 : INVALID_INPUT
        @Test
        void shouldReturnInvalidInputWhenUsernameIsNull() {

            String username = null;
            String password = "StrongPass1";


            RegistrationStatus result = accountManager.registerUser(username, password);


            assertEquals(RegistrationStatus.INVALID_INPUT, result);

            verify(passwordEncoder, never()).encode(any());
            verify(userRepository, never()).saveUser(any(), any());
            verify(logger, never()).logInfo(any());
        }

        @Test
        void shouldReturnInvalidInputWhenPasswordIsEmpty() {

            String username = "validUser";
            String password = "";


            RegistrationStatus result = accountManager.registerUser(username, password);


            assertEquals(RegistrationStatus.INVALID_INPUT, result);

            verify(passwordEncoder, never()).encode(any());
            verify(userRepository, never()).saveUser(any(), any());
            verify(logger, never()).logInfo(any());
        }

        // US-03 : INVALID_FORMAT
        @Test
        void shouldReturnInvalidFormatWhenUsernameTooShort() {

            String username = "abc"; // only 3 chars
            String password = "StrongPass1";


            RegistrationStatus result = accountManager.registerUser(username, password);


            assertEquals(RegistrationStatus.INVALID_FORMAT, result);

            verify(passwordEncoder, never()).encode(any());
            verify(userRepository, never()).saveUser(any(), any());
            verify(logger, never()).logInfo(any());
        }

        @Test
        void shouldReturnInvalidFormatWhenPasswordTooShort() {
            String username = "validUser";
            String password = "Abc12"; // too short

            RegistrationStatus result = accountManager.registerUser(username, password);

            assertEquals(RegistrationStatus.INVALID_FORMAT, result);

            verify(passwordEncoder, never()).encode(any());
            verify(userRepository, never()).saveUser(any(), any());
            verify(logger, never()).logInfo(any());
        }

        @Test
        void shouldReturnInvalidFormatWhenPasswordHasNoDigit() {
            String username = "validUser";
            String password = "Password"; // no number

            RegistrationStatus result = accountManager.registerUser(username, password);

            assertEquals(RegistrationStatus.INVALID_FORMAT, result);

            verify(passwordEncoder, never()).encode(any());
            verify(userRepository, never()).saveUser(any(), any());
            verify(logger, never()).logInfo(any());
        }

        // US-04 : USER ALREADY EXISTS
        @Test
        void shouldReturnInvalidUsernameAlreadyExistsWhenUserAlreadyExists() {

            String username = "existingUser";
            String password = "StrongPass1";

            when(userRepository.userExists(username)).thenReturn(true);


            RegistrationStatus result = accountManager.registerUser(username, password);


            assertEquals(RegistrationStatus.INVALID_USERNAME_ALREADY_EXISTS, result);

            verify(userRepository, times(1)).userExists(username);
            verify(passwordEncoder, never()).encode(any());
            verify(userRepository, never()).saveUser(any(), any());
            verify(logger, never()).logInfo(any());
        }

    }

