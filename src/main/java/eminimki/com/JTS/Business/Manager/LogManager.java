package eminimki.com.JTS.Business.Manager;

import eminimki.com.JTS.Business.Services.LogService;
import eminimki.com.JTS.Business.requests.LogRequests.CreateLogRequest;
import eminimki.com.JTS.Business.requests.LogRequests.UpdateLogRequest;
import eminimki.com.JTS.Business.responses.LogResponse.GetAllLogByActionerResponse;
import eminimki.com.JTS.Business.responses.LogResponse.GetAllLogByUsernameResponse;
import eminimki.com.JTS.Business.responses.LogResponse.GetAllLogResponse;
import eminimki.com.JTS.Business.responses.LogResponse.GetLogByIdResponse;
import eminimki.com.JTS.Common.Utilities.CommonUtils;
import eminimki.com.JTS.Common.Utilities.Mapper.ModelMapperService;
import eminimki.com.JTS.DataAccess.JTSLogRepository;
import eminimki.com.JTS.Entities.JTSLog;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LogManager implements LogService {

    private JTSLogRepository jtsLogRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<GetAllLogResponse> getAll() {
        List<JTSLog> jtsLogs = jtsLogRepository.findAll();
        List<GetAllLogResponse> logResponses = jtsLogs.stream()
                .map(task -> this.modelMapperService.forResponse().map(task, GetAllLogResponse.class))
                .collect(Collectors.toList());

        return logResponses;
    }

    @Override
    public List<GetAllLogByActionerResponse> getAllLogByActioner(String actioner) {
        List<JTSLog> jtsLogs = jtsLogRepository.findByActioner(actioner);
        List<GetAllLogByActionerResponse> jtsLogResponses = jtsLogs.stream()
                .map(jtsLog -> this.modelMapperService.forResponse().map(jtsLog, GetAllLogByActionerResponse.class))
                .collect(Collectors.toList());

        return jtsLogResponses;
    }

    @Override
    public List<GetAllLogByUsernameResponse> getAllLogByUsername(String username) {
        List<JTSLog> jtsLogs = jtsLogRepository.findByActionerContainingOrDescriptionContaining(username, username);
        List<GetAllLogByUsernameResponse> jtsLogResponses = jtsLogs.stream()
                .map(jtsLog -> this.modelMapperService.forResponse().map(jtsLog, GetAllLogByUsernameResponse.class))
                .collect(Collectors.toList());

        return jtsLogResponses;
    }

    @Override
    public GetLogByIdResponse getByID(int logId) {
        JTSLog jtsLog = this.jtsLogRepository.findById(logId).orElseThrow();
        GetLogByIdResponse getLogByIdResponse =  this.modelMapperService.forResponse().map(jtsLog, GetLogByIdResponse.class);
        return getLogByIdResponse;
    }

    @Override
    public void add(CreateLogRequest createLogRequest) {
        String currentTime = CommonUtils.getCurrentDateTimeFormatted();
        JTSLog jtsLog = this.modelMapperService.forRequest().map(createLogRequest,JTSLog.class);
        jtsLog.setTime(currentTime);
        jtsLog.setCreatedTime(currentTime);
        jtsLog.setModifiedTime(currentTime);
        this.jtsLogRepository.save(jtsLog);
    }

    @Override
    public void update(UpdateLogRequest updateLogRequest) {
        String currentTime = CommonUtils.getCurrentDateTimeFormatted();
        JTSLog jtsLog = this.modelMapperService.forRequest().map(updateLogRequest,JTSLog.class);
        jtsLog.setModifiedTime(currentTime);
        this.jtsLogRepository.save(jtsLog);
    }

    @Override
    public void delete(int id) {
        this.jtsLogRepository.deleteById(id);
    }


    // Method logs

    @Override
    public void add(String actioner, String description) {
        String currentTime = CommonUtils.getCurrentDateTimeFormatted();
        JTSLog jtsLog = new JTSLog();
        jtsLog.setActioner(actioner);
        jtsLog.setDescription(description);
        jtsLog.setTime(currentTime);
        jtsLog.setCreatedTime(currentTime);
        jtsLog.setModifiedTime(currentTime);
        this.jtsLogRepository.save(jtsLog);
    }
}
