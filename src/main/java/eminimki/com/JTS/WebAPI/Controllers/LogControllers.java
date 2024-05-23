package eminimki.com.JTS.WebAPI.Controllers;

import eminimki.com.JTS.Business.Services.LogService;
import eminimki.com.JTS.Business.requests.LogRequests.CreateLogRequest;
import eminimki.com.JTS.Business.requests.LogRequests.UpdateLogRequest;
import eminimki.com.JTS.Business.responses.LogResponse.GetAllLogByActionerResponse;
import eminimki.com.JTS.Business.responses.LogResponse.GetAllLogByUsernameResponse;
import eminimki.com.JTS.Business.responses.LogResponse.GetAllLogResponse;
import eminimki.com.JTS.Business.responses.LogResponse.GetLogByIdResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@AllArgsConstructor
public class LogControllers {
    private LogService logService;

    @GetMapping("/get/all")
    List<GetAllLogResponse> getAll(){return logService.getAll();}

    @GetMapping("/get/{logId}")
    public GetLogByIdResponse getById(@PathVariable int logId){
        return logService.getByID(logId);
    }

    @GetMapping("/get/actioner/{actioner}")
    public List<GetAllLogByActionerResponse> getAllLogByActioner(@PathVariable String actioner){
        return logService.getAllLogByActioner(actioner);
    }

    @GetMapping("/get/username/{username}")
    public ResponseEntity<List<GetAllLogByUsernameResponse>> getAllLogByUsername(@PathVariable String username){
        return ResponseEntity.ok().body(this.logService.getAllLogByUsername(username));
    }

    @PostMapping("/post/add")
    public void add(@RequestBody CreateLogRequest createLogRequest){
        this.logService.add(createLogRequest);
    }

    @PutMapping("/put/update")
    public void update(@RequestBody UpdateLogRequest updateLogRequest){this.logService.update(updateLogRequest);}

    @DeleteMapping("/delete/{logId}")
    public void delete(@PathVariable int logId ){
        this.logService.delete(logId);
    }
}
