package app.controller;

import app.model.dto.CreateContract;
import app.model.dto.LoginSession;
import app.model.view.BlockchainView;
import app.model.view.ContractView;
import app.model.view.KeysView;
import org.springframework.core.io.InputStreamSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import static app.service.BlockManager.BLOCKCHAIN;
import static app.service.BlockManager.MineBlock;
import static app.service.ContractManager.Contracts;
import static app.service.ContractManager.CreateContractUTXO;
import static app.service.KeyzManager.CreateAndStoreKey;
import static app.service.KeyzManager.KEYS;
import static app.service.MailService.SendMail;
import static app.service.PasskitService.CreatePass;
import static app.service.UserManager.isValidSession;
import static app.utils.JsonUtils.ToJSON;

@CrossOrigin(origins = {"*"}, methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS, RequestMethod.HEAD, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.PUT})
@EnableScheduling
@RestController
public class BlockchainController {

    @Scheduled(fixedRate = 5000)
    public void createBlock() {
        System.out.println("Mining active");
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
    public String createContract(@RequestBody CreateContract createContract) {
        CreateContractUTXO(createContract);
        return "";
    }

    @PostMapping(value = "/contracts")
    public String contracts(@RequestBody LoginSession loginSession) {
        if (isValidSession(loginSession)) {
            return ToJSON(new ContractView(Contracts));
        }
        return "";
    }


    @PostMapping(value = "/blocks")
    public String blocks(@RequestBody LoginSession loginSession) {
        if (isValidSession(loginSession)) {
            return ToJSON(new BlockchainView(BLOCKCHAIN));
        }
        return "";
    }

    @GetMapping(value="testMail")
    public String testMail() {
        InputStreamSource hello = CreatePass("hello");
        SendMail("devasood@gmail.com","test", "hi", hello);
        return "Success";
    }


}
