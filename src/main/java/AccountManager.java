public class AccountManager {
        private final IUserRepository userRepository;
        private final IPasswordEncoder passwordEncoder;
        private final ILogger logger;

        public AccountManager(IUserRepository userRepository,
                              IPasswordEncoder passwordEncoder,
                              ILogger logger) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.logger = logger;
        }

    public RegistrationStatus registerUser(String username, String rawPassword) {
        // hamza will implement this later
        return RegistrationStatus.SUCCESS; // temporary just so it compiles
    }
    }

