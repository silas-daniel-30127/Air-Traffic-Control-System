package atc.Service;

import atc.DTO.UserDTO;
import atc.Model.User;
import atc.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepo userRepo, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public UserDTO addUser(UserDTO userDTO) {

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        User savedUser = userRepo.save(user);

        UserDTO returnedUser = new UserDTO();
        returnedUser.setUsername(savedUser.getUsername());
        returnedUser.setPassword(savedUser.getPassword());
        returnedUser.setId(savedUser.getId());
        return returnedUser;
    }

    public String deleteUserById(int id) {
        if (userRepo.findById(id).isPresent()) {
            userRepo.deleteById(id);
            return "User " + id + " removed!!";
        }
        return "Not found user: " + id + " !";
    }

    public UserDTO updateUserById(int id, UserDTO userDTO) {
        User existingUser = userRepo.findById(id).orElse(null);
        assert existingUser != null;

        if (userDTO.getUsername() != null) {
            existingUser.setUsername(userDTO.getUsername());
        }

        if (userDTO.getPassword() != null) {
            existingUser.setPassword(userDTO.getPassword());
        }
        User savedUser = userRepo.save(existingUser);
        UserDTO returnedUser = new UserDTO();
        returnedUser.setUsername(savedUser.getUsername());
        returnedUser.setPassword(savedUser.getPassword());
        returnedUser.setId(savedUser.getId());
        return returnedUser;
    }

    public List<UserDTO> getUsers() {
        List<UserDTO> userDTOS = new ArrayList<>();
        List<User> users = (List<User>) userRepo.findAll();

        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword(user.getPassword());
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }

    public UserDTO getUserById(int id) {
        User user = userRepo.findById(id).orElse(null);
        UserDTO userDTO = new UserDTO();

        assert user != null;
        userDTO.setPassword(user.getPassword());
        userDTO.setUsername(user.getUsername());
        userDTO.setId(user.getId());
        return userDTO;
    }

    public boolean getConnection(UserDTO userDTO) {
        User userFromData = userRepo.findUserByUsername(userDTO.getUsername());
        if (userFromData != null) {
            return bCryptPasswordEncoder.matches(userDTO.getPassword(), userFromData.getPassword());
        }
        return false;
    }
}
