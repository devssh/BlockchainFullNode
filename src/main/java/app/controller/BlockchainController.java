package app.controller;

import app.model.dto.CreateContract;
import app.model.dto.LoginSession;
import app.model.view.BlockchainView;
import app.model.view.ContractView;
import app.model.view.KeysView;
import app.model.view.TransactionView;
import org.springframework.core.io.InputStreamSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import static app.model.dto.FullUserData.FULL;
import static app.model.dto.FullUserData.REDEEM;
import static app.model.dto.FullUserData.VIEW;
import static app.service.BlockManager.BLOCKCHAIN;
import static app.service.BlockManager.MineBlock;
import static app.service.ContractManager.Contracts;
import static app.service.ContractManager.CreateContractUTXO;
import static app.service.KeyzManager.CreateAndStoreKey;
import static app.service.KeyzManager.KEYS;
import static app.service.MailService.SendMail;
import static app.service.PasskitService.CreatePass;
import static app.service.TransactionManager.*;
import static app.service.UserManager.isValidSession;
import static app.utils.JsonUtils.ToJSON;

@CrossOrigin(origins = {"*"}, methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.PUT})
@EnableScheduling
@RestController
public class BlockchainController {

    @Scheduled(fixedRate = 5000)
    public void createBlock() {
        MineBlock();
    }

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

    @PostMapping(value = "/createContract")
    public String createContractController(@RequestBody CreateContract createContract) {
        LoginSession loginSession = new LoginSession(createContract.email, createContract.sessionToken);
        if (isValidSession(loginSession, new String[]{FULL})) {
            CreateContractUTXO(createContract);
        }
        return "";
    }

    @PostMapping(value = "/createTransaction")
    public String createTransaction(@RequestBody CreateContract createContract) {
        LoginSession loginSession = new LoginSession(createContract.email, createContract.sessionToken);
        if (isValidSession(loginSession, new String[]{FULL, VIEW})) {
            CreateCreateTransactionUTXO(createContract);
        }
        return "";
    }

    @PostMapping(value = "/completeTransaction")
    public String completeTransaction(@RequestBody CreateContract createContract) {
        LoginSession loginSession = new LoginSession(createContract.email, createContract.sessionToken);
        if (isValidSession(loginSession, new String[]{FULL, REDEEM})) {
            try {
                return "{\"complete\":" + CreateCompleteTransactionUTXO(createContract) + "}";
            } catch (IllegalArgumentException e) {
                return "{\"complete\":\"doubleSpendDetected\"}";
            }
        }
        System.out.println("whyyy" + createContract.toString());
        System.out.println("whyyy" + createContract.toString());
        return "{\"complete\":false}";
    }

    @PostMapping(value = "/contracts")
    public String contracts(@RequestBody LoginSession loginSession) {
        if (isValidSession(loginSession, new String[]{FULL, VIEW, REDEEM})) {
            return ToJSON(new ContractView(Contracts));
        }
        return "";
    }


    @PostMapping(value = "/transactions")
    public String transactions(@RequestBody LoginSession loginSession) {
        if (isValidSession(loginSession, new String[]{FULL, VIEW, REDEEM})) {
            return ToJSON(new TransactionView(Transactions));
        }
        return "";
    }


    @PostMapping(value = "/blocks")
    public String blocks(@RequestBody LoginSession loginSession) {
        if (isValidSession(loginSession, new String[]{FULL, VIEW, REDEEM})) {
            return ToJSON(new BlockchainView(BLOCKCHAIN));
        }
        return "";
    }
//
//    @GetMapping(value = "testMail")
//    public String testMail() {
//        InputStreamSource hello = CreatePass("hello,xyz@gmail.com,pxt", "Offer", "Pedigree Dog food", "5% off");
//        SendMail("xyz@gmail.com", "Discount Coupon",
//                "Scan the code below to claim a discount of " + "on " + ".", hello);
//        return "Success";
//    }


}
