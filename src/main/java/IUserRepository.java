    public interface IUserRepository {
        boolean userExists(String username);
        void saveUser(String username, String hashedPassword);
    }
