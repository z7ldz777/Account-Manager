    import org.junit.jupiter.api.Test;
    import static org.junit.jupiter.api.Assertions.*;
    import static org.mockito.Mockito.*;

    public class AccountManagerTest {

        private final IUserRepository userRepository = mock(IUserRepository.class);
        private final IPasswordEncoder passwordEncoder = mock(IPasswordEncoder.class);
        private final ILogger logger = mock(ILogger.class);

        private final AccountManager accountManager =
                new AccountManager(userRepository, passwordEncoder, logger);

        @Test
        void sampleTest() {
            assertTrue(true); // zaid will work here
        }
    }

