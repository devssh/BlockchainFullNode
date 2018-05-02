package app.controller;

import app.model.dto.CreateContract;
import app.model.dto.LoginSession;
import app.model.view.BlockchainView;
import app.model.view.KeysView;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import static app.service.BlockManager.BLOCKCHAIN;
import static app.service.BlockManager.MineBlock;
import static app.service.ContractManager.CreateContractUTXO;
import static app.service.KeyzManager.CreateAndStoreKey;
import static app.service.KeyzManager.KEYS;
import static app.service.UserManager.isValidSession;
import static app.utils.JsonUtils.ToJSON;

@CrossOrigin(origins = {"http://localhost:3000"}, methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS})
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
    public String contracts() {
        return "";
    }

    @GetMapping(value="/blockchain")
    public String blockchain() {
        return ToJSON(new BlockchainView(BLOCKCHAIN));
    }


    @PostMapping(value = "/blocks")
    public String blocks(@RequestBody LoginSession loginSession) {
        if (isValidSession(loginSession)) {
            return "{\"hi\":\"hello\"}";
        }
        return "";
    }


}
