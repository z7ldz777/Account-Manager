public class AccountManager {

    // Dependencies
    private final IUserRepository userRepository;
    private final IPasswordEncoder passwordEncoder;
    private final ILogger logger;

    // Validation rules
    private static final int USERNAME_MIN = 5;
    private static final int USERNAME_MAX = 20;
    private static final int PASSWORD_MIN = 8;

    public AccountManager(IUserRepository userRepository,
                          IPasswordEncoder passwordEncoder,
                          ILogger logger) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.logger = logger;
    }

    public RegistrationStatus registerUser(String username, String rawPassword) {

        if (isNullOrEmpty(username) || isNullOrEmpty(rawPassword)) {
            return RegistrationStatus.INVALID_INPUT; // us-02
        }

        if (!isValidUsername(username) || !isValidPassword(rawPassword)) {
            return RegistrationStatus.INVALID_FORMAT; // us-03
        }

        if (userRepository.userExists(username)) {
            return RegistrationStatus.INVALID_USERNAME_ALREADY_EXISTS; // us-04
        }

        // us-01: SUCCESS
        String hashed = passwordEncoder.encode(rawPassword);
        userRepository.saveUser(username, hashed);
        logger.logInfo("User registered: " + username);

        return RegistrationStatus.SUCCESS;
    }

    // -----------------------
    // Validation Helpers
    // -----------------------

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isValidUsername(String username) {
        int len = username.length();
        return len >= USERNAME_MIN && len <= USERNAME_MAX;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= PASSWORD_MIN && containsDigit(password);
    }

    private boolean containsDigit(String value) {
        for (char c : value.toCharArray()) {
            if (Character.isDigit(c)) return true;
        }
        return false;
    }
}
