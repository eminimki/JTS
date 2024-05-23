package eminimki.com.JTS.WebAPI.Controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorControllers implements ErrorController {

    @RequestMapping("/403")
    public String accessDeniedError() {
        return "common/errorPages/403"; // Örnek bir hata sayfasının adı
    }

    @RequestMapping("/404")
    public String notFoundError() {
        return "common/errorPages/404"; // Örnek bir hata sayfasının adı
    }



}
