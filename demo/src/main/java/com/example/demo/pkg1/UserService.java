package com.example.demo.pkg1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.exception.UserNotFoundException;

@Service
public class UserService {

	private final Map<Long, User> users;

    public UserService() {
        // Initialize some dummy users
        users = new HashMap<>();
        users.put(1L, new User(1L, "john_doe", "john@example.com"));
        users.put(2L, new User(2L, "jane_smith", "jane@example.com"));
        users.put(3L, new User(3L, "user1", "user1@example.com"));
        users.put(4L, new User(4L, "user2", "user2@example.com"));
        users.put(5L, new User(5L,  "user3", "user3@example.com"));
        
       }

    public User getUserById(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        return user;
    }
    
 
}
