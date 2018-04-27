package app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static app.model.Keyz.GenerateKey;
import static app.model.Keyz.GenerateSeed;

@RestController
public class BlockchainController {
    @GetMapping(value = "/")
    public String blockExplorer() {
        GenerateKey(GenerateSeed());
        return "hello world";
    }

}
