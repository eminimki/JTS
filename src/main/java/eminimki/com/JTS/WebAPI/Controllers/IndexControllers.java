package eminimki.com.JTS.WebAPI.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexControllers {

    @GetMapping("/index")
    public String index(){
        return "index";
    }


}
