package atc.API;

import atc.DTO.UserDTO;
import atc.Model.Token;
import atc.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final Token token;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        token = new Token(false);
    }

    @PostMapping("/login")
    public Token login(@RequestBody UserDTO user) {

        if (userService.getConnection(user)) {
            token.setAccess(true);
            System.out.println("You are in !!");
            this.token.setAccess(true);
//            TODO: redirect to the atc controller
        } else {
            this.token.setAccess(false);
            System.out.println("Incorrect username or password");
        }
        return token;
    }

    @PostMapping(path = "/crud")
    public UserDTO addUser(@RequestBody @NotBlank UserDTO userDTO) {
        return userService.addUser(userDTO);
    }


    @DeleteMapping(path = "/crud/{id}")
    public String deleteUserById(@PathVariable int id) {
        return userService.deleteUserById(id);
    }

    @PutMapping(path = "/crud/{id}")
    public UserDTO updateUserById(@PathVariable int id, @RequestBody UserDTO userDTO) {
        return userService.updateUserById(id, userDTO);
    }

    @GetMapping("/crud")
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(path = "/crud/{id}")
    public UserDTO getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

}
