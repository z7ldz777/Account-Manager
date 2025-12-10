public class AccountManager {
        private final IUserRepository userRepository;
        private final IPasswordEncoder passwordEncoder;
        private final ILogger logger;
    private static final int USERNAME_MIN = 5;  // Example value
    private static final int USERNAME_MAX = 20; // Example value
    private static final int PASSWORD_MIN = 8;  // Example value based on your code further down

        public AccountManager(IUserRepository userRepository,
                              IPasswordEncoder passwordEncoder,
                              ILogger logger) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.logger = logger;
        }

    public RegistrationStatus registerUser(String username, String rawPassword) {
        // us-02 : INVALID_INPUT (null or empty username/password)
        if (username == null || username.trim().isEmpty()
                || rawPassword == null || rawPassword.trim().isEmpty()) {
            return RegistrationStatus.INVALID_INPUT;
        }

        // us-03 : INVALID_FORMAT (Username)
        if (username.length() < USERNAME_MIN || username.length() > USERNAME_MAX) {
            return RegistrationStatus.INVALID_FORMAT;
        }

        // us-03 : INVALID_FORMAT (Password)
        if (rawPassword.length() < PASSWORD_MIN || !containsDigit(rawPassword)) {
            return RegistrationStatus.INVALID_FORMAT;
        }

        // us-04 : Username already exists
        if (userRepository.userExists(username)) {
            return RegistrationStatus.INVALID_USERNAME_ALREADY_EXISTS;
        }

        //  us-01: SUCCESS
        String hashedPassword = passwordEncoder.encode(rawPassword);
        userRepository.saveUser(username, hashedPassword);
        logger.logInfo("User registered: " + username);

        return RegistrationStatus.SUCCESS;
    }

    private boolean containsDigit(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;

    }
    }

