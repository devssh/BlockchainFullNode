package app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static app.model.Keyz.generateKey;

@RestController
public class BlockchainController {
    @GetMapping(value = "/")
    public String blockExplorer() {
        generateKey();
        return "hello world";
    }

}
