package in.wb.ims.controller;


import in.wb.ims.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import in.wb.ims.model.request.CreateUserRequest;
import in.wb.ims.model.request.CreateLoginRequest;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {



    @Autowired
    private UserService userService;


/*    @PostMapping("/createUser")
    @RolesAllowed("admin")
    public ResponseEntity<?> createUser() {
        return createUser(null);
    }*/

    @PostMapping("/createUser")
    @RolesAllowed("admin")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest request) {

        try {
            return ResponseEntity.ok(userService.createUser(request));
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to Register a User", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@Valid @RequestBody CreateLoginRequest request) throws Exception {

      Map<String,Object> loginInfo =  userService.login(request);

       if(loginInfo.get("STATUS").equals("SUCCESS")){
          return ResponseEntity.ok(loginInfo);
       } else {
           return new ResponseEntity<>(loginInfo,HttpStatus.BAD_REQUEST);
       }


    }

    @GetMapping("/test")
    public String ping(){
        return "Test is successfull";
    }

}
