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

        public void register(String username, String password) {
            // hamza will work here
        }

        public boolean login(String username, String password) {
            // hamza will work here
            return false;
        }
    }

