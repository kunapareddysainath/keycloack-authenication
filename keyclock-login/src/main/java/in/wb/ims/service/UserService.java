package in.wb.ims.service;

import in.wb.ims.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import in.wb.ims.model.document.User;
import in.wb.ims.model.request.CreateUserRequest;
import in.wb.ims.model.request.CreateLoginRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${keycloak.realm}")
    private String realm;

    public User createUser(CreateUserRequest request) {

        User user = new User();

        user.setEmail(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());

        return userRepository.save(user);
    }

    public Map<String, Object> login(CreateLoginRequest loginDetails) {


        Map<String, Object> loginResponse = new HashMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> credentials = new LinkedMultiValueMap<>();
        credentials.add("username", loginDetails.getUsername());
        credentials.add("password", loginDetails.getPassword());
        credentials.add("client_id", "wb-ims-login");
        credentials.add("grant_type", "password");
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(credentials, headers);
        try {
            ResponseEntity<Object> response = restTemplate.exchange(prepareUrl( "Token"), HttpMethod.POST, entity, Object.class);
            loginResponse.put("STATUS", "SUCCESS");
            loginResponse.put("token_details_string", response.getBody());
            return loginResponse;
        } catch (HttpClientErrorException exception) {
            Map<String, Object> error = new HashMap<>();
            error.put("STATUS","FAILED");
            error.put("message", "Please provide Valid user name and password");
            return error;
        }
    }

    public String prepareUrl(String requiredUrl) {
        switch (requiredUrl) {
            case "Token":
                return "http://localhost:8080/realms/wb-ims-login/protocol/openid-connect/token";
            default:
                return "";
        }
    }


    public boolean validateUser(CreateLoginRequest request) {

        User existingUser = userRepository.findByEmail(request.getUsername()).orElse(null);

        if (existingUser == null) {
            return false;
        }
        return existingUser.getPassword().equals(request.getPassword());
    }
}
