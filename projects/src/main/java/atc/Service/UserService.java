package atc.Service;

import atc.Model.User;
import atc.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void addUser(User user) {
        userRepo.save(user);
    }

    public String deleteUserById(int id) {
        if (userRepo.findById(id).isPresent()) {
            userRepo.deleteById(id);
            return "User " + id + " removed!!";
        }
        return "Not found user: " + id + " !";
    }

    public User updateUserById(int id, User user) {
        User existingUser = userRepo.findById(id).orElse(null);
        assert existingUser != null;

        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }

        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }
        return userRepo.save(existingUser);
    }

    public List<User> getUsers() {
        return (List<User>) userRepo.findAll();
    }

    public User getUserById(int id) {
        return userRepo.findById(id).orElse(null);
    }

    public boolean getConnection(User user) {
        User userFromData = userRepo.findUserByUsername(user.getUsername());
        if (userFromData != null) {
            return userFromData.getPassword().equals(user.getPassword());
        }
        return false;
    }


}
