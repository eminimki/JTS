package eminimki.com.JTS.Business.Services;

import eminimki.com.JTS.Business.requests.LogRequests.CreateLogRequest;
import eminimki.com.JTS.Business.requests.LogRequests.UpdateLogRequest;
import eminimki.com.JTS.Business.responses.LogResponse.GetAllLogByUsernameResponse;
import eminimki.com.JTS.Business.responses.LogResponse.GetAllLogByActionerResponse;
import eminimki.com.JTS.Business.responses.LogResponse.GetAllLogResponse;
import eminimki.com.JTS.Business.responses.LogResponse.GetLogByIdResponse;

import java.util.List;

public interface LogService {

    // Controller logs
    List<GetAllLogResponse> getAll();
    List<GetAllLogByActionerResponse> getAllLogByActioner(String actioner);
    List<GetAllLogByUsernameResponse> getAllLogByUsername(String username);

    GetLogByIdResponse getByID(int logId);

    void add(CreateLogRequest createLogRequest);
    void update(UpdateLogRequest updateLogRequest);
    void delete(int id);


    //Method logs
    void add(String actioner, String description);



}
