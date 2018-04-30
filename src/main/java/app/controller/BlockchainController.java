package app.controller;

import app.model.view.KeysView;
import app.model.dto.Activation;
import app.model.dto.FullUserData;
import app.model.dto.LoginDetails;
import app.model.dto.LoginSession;
import app.model.view.RegistrationPendingView;
import app.model.view.UsersView;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static app.model.Keyz.GenerateSeed;
import static app.service.KeyzManager.CreateAndStoreKey;
import static app.service.KeyzManager.KEYS;
import static app.service.MailService.SendMailWithConfirmationCode;
import static app.service.RegistrationManager.RegistrationCodes;
import static app.service.RegistrationManager.RegistrationPendingUsers;
import static app.service.UserManager.Users;
import static app.utils.JsonUtils.ToJSON;

@CrossOrigin(origins = {"http://localhost:3000"}, methods = {RequestMethod.POST, RequestMethod.GET})
@RestController
public class BlockchainController {
    @GetMapping(value = "/")
    public String home() {
        return "Server is running";
    }

    @GetMapping(value = "/keys")
    public String keys() {
        return ToJSON(new KeysView(KEYS));
    }

    @GetMapping(value = "/createKey/{name}")
    public String blockExplorers(@PathVariable("name") String name) {
        return CreateAndStoreKey(name);
    }

    @GetMapping(value = "/blocks")
    public String blocks() {
        return "";
    }

    @GetMapping(value = "/verification-mail/{email}")
    public String sendEmail(@PathVariable("email") String email) {
        SendMailWithConfirmationCode(email, "Hi", "Works");
        return "{\"mailSent\":true}";
    }

    @PostMapping(value = "/register")
    public String register(@RequestBody LoginDetails loginDetails) {
        if (RegistrationPendingUsers.containsKey(loginDetails.email)) {
            return "{registration: \"reattempted\"}";
        }
        //TODO: check users as well
        RegistrationPendingUsers.putIfAbsent(loginDetails.email, loginDetails);
        RegistrationCodes.putIfAbsent(loginDetails.email, GenerateSeed(6));
        SendMailWithConfirmationCode(loginDetails.email, "Activate your Account",
                "Your activation code is " + RegistrationCodes.get(loginDetails.email));
        return "{registration: true}";
    }

    @PostMapping(value = "complete-registration")
    public String completeRegistration(@RequestBody Activation activation) {
        if (RegistrationCodes.get(activation.email).equals(activation.activationCode)) {
            LoginDetails loginDetails = RegistrationPendingUsers.get(activation.email);
            String sessionToken = GenerateSeed(6);
            Users.putIfAbsent(loginDetails.email, new FullUserData(loginDetails.email, loginDetails.password,
                    sessionToken, LocalDateTime.now().plusDays(1).toString()));
            RegistrationPendingUsers.remove(loginDetails.email);
            RegistrationCodes.remove(loginDetails.email);
            return "{sessionToken: \"" + sessionToken + "\"}";
        }
        return "{registration: false}";
    }

    @PostMapping(value = "login-session")
    public String login(@RequestBody LoginSession loginSession) {
        FullUserData user = Users.get(loginSession.email);
        if (loginSession.email.equals(user.email) && loginSession.sessionToken.equals(user.sessionToken)) {
            //TODO: validate sessionToken valid and refresh expiry
            return "{login: true}";
        }

        return "{login: false}";
    }

    @PostMapping(value = "login")
    public String login(@RequestBody LoginDetails loginDetails) {
        FullUserData user = Users.get(loginDetails.email);
        if (loginDetails.email.equals(user.email) && loginDetails.password.equals(user.password)) {
            //TODO: refresh expiry if expired
            return "{sessionToken: \"" + user.sessionToken + "\"}";
        }
        return "{login: false}";
    }

    @GetMapping(value = "users")
    public String usersView() {
        return ToJSON(new UsersView(Users));
    }

    @GetMapping(value = "activationPendingUsers")
    public String registrationView() {
        return ToJSON(new RegistrationPendingView(RegistrationPendingUsers));
    }
}
