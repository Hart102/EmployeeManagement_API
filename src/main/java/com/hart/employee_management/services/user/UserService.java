package com.hart.employee_management.services.user;

import com.hart.employee_management.exception.CustomException;
import com.hart.employee_management.model.User;
import com.hart.employee_management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;

    // Get user by Id
    // Update User

    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new CustomException("User with email: " + user.getEmail() + " already exist");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        var updatedUser = new User();
        updatedUser.setEmail(user.getEmail());
        return userRepository.save(updatedUser);
    }

    @Override
    public User findUserByEmail(String email) {
        var user =  userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException("User not found!");
        }
        return user;
    }
}
