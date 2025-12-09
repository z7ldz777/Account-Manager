public enum RegistrationStatus {
    SUCCESS,
    INVALID_INPUT, // Use for null or empty strings
    INVALID_FORMAT, // Use for length or complexity violations
    INVALID_USERNAME_ALREADY_EXISTS // Username already exists
}

