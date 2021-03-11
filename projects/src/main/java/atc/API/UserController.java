package atc.API;

import atc.Model.Token;
import atc.Model.User;
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
    public Token login(@RequestBody User user) {

        if (userService.getConnection(user)) {
            token.setAccess(true);
            System.out.println("You are in !!");
            this.token.setAccess(true);
//            TODO: redirect to the atc controller
        } else {
            System.out.println("Incorrect username or password");
        }
        return token;
    }

    @GetMapping("/logout")
    public void logout() {
        token.setAccess(false);
        System.out.println("Good bye!");
//        TODO: redirect to the login dialog

    }


    @PostMapping(path = "/crud")
    public void addUser(@RequestBody @NotBlank User user) {
        userService.addUser(user);
    }


    @DeleteMapping(path = "/crud/{id}")
    public void deleteUserById(@PathVariable int id) {
        userService.deleteUserById(id);
    }

    @PutMapping(path = "/crud/{id}")
    public void updateUserById(@PathVariable int id, @RequestBody User user) {
        userService.updateUserById(id, user);
    }

    @GetMapping("/crud")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(path = "/crud/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

}
