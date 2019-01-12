package services;

import model.User;

public interface UserService {
    User getUserByEmail(String email);
    boolean existsByEmail(String email);
    void addUser(User user);
    void updateUser(User user);
}
