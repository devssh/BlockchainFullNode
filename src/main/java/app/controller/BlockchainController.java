package app.controller;

import app.model.KeysView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static app.service.KeyzManager.CreateAndStoreKey;
import static app.service.KeyzManager.KEYS;
import static app.utils.JsonUtils.ToJSON;

@RestController
public class BlockchainController {

    @GetMapping(value = "/keys")
    public String keys() {
        return ToJSON(new KeysView(KEYS));
    }

    @GetMapping(value="/createKey/{name}")
    public String blockExplorers(@PathVariable("name") String name) {
        return CreateAndStoreKey(name);
    }

    @GetMapping(value = "/blocks")
    public String blocks() {
        return "";
    }

}
