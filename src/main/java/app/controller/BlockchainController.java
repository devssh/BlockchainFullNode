package app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlockchainController {
    @GetMapping(value = "/")
    public String blockExplorer() {
        return "hello world";
    }

}
