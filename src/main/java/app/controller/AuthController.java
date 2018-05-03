package app.controller;

import app.model.dto.Activation;
import app.model.dto.FullUserData;
import app.model.dto.LoginDetails;
import app.model.dto.LoginSession;
import app.model.view.RegistrationPendingView;
import app.model.view.UsersView;
import org.springframework.web.bind.annotation.*;

import static app.service.MailService.SendMail;
import static app.service.MailService.SendMailWithConfirmationCodes;
import static app.service.RegistrationManager.CreateAndStoreRegistration;
import static app.service.RegistrationManager.RegistrationPendingUsers;
import static app.service.UserManager.CreateAndStoreUserIfActivated;
import static app.service.UserManager.Users;
import static app.utils.JsonUtils.ToJSON;

@CrossOrigin(origins = {"*"}, methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.PUT})
@RestController
public class AuthController {

    @GetMapping(value = "/verification-mail/{email}")
    public String sendEmail(@PathVariable("email") String email) {
        SendMail(email, "Hi", "Works");
        return "{\"mailSent\":true}";
    }

    @PostMapping(value = "/register")
    public String register(@RequestBody LoginDetails loginDetails) {
        if (RegistrationPendingUsers.containsKey(loginDetails.email)) {
            return "{\"registration\": \"reattempted\"}";
        }
        //TODO: check users as well
        CreateAndStoreRegistration(loginDetails);
        SendMailWithConfirmationCodes(loginDetails.email);
        return "{\"registration\": true}";
    }

    @PostMapping(value = "complete-registration")
    public String completeRegistration(@RequestBody Activation activation) {
        try {
            LoginSession loginSession = CreateAndStoreUserIfActivated(activation);
            return "{\"sessionToken\": \"" + loginSession.sessionToken + "\"}";
        } catch (NullPointerException e) {
            return "{\"registration\": false}";
        }
    }

    @PostMapping(value = "login")
    public String login(@RequestBody LoginDetails loginDetails) {
        FullUserData user = Users.get(loginDetails.email);
        if (user != null && loginDetails.email.equals(user.email) && loginDetails.password.equals(user.password)) {
            //TODO: refresh expiry if expired
            return "{\"sessionToken\": \"" + user.sessionToken + "\"}";
        }

        LoginDetails loginDetailsExpected = RegistrationPendingUsers.get(loginDetails.email);
        if (loginDetailsExpected != null && loginDetails.email.equals(loginDetailsExpected.email) &&
                loginDetails.password.equals(loginDetailsExpected.password)) {
            return "{\"activation\": \"pending\"}";
        }
        return "{\"login\": false}";
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
