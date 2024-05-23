package eminimki.com.JTS.Business.Manager;

import eminimki.com.JTS.Business.Rules.TaskBusinessRules;
import eminimki.com.JTS.Business.Services.TaskService;
import eminimki.com.JTS.Business.requests.TaskRequests.CreateTaskRequest;
import eminimki.com.JTS.Business.requests.TaskRequests.UpdateTaskRequest;
import eminimki.com.JTS.Business.requests.TaskRequests.UpdateTaskStatusRequest;
import eminimki.com.JTS.Business.responses.TaskResponses.*;
import eminimki.com.JTS.Common.Utilities.CommonUtils;
import eminimki.com.JTS.Common.Utilities.Mapper.ModelMapperService;
import eminimki.com.JTS.Common.Utilities.Manager.EmailManager;
import eminimki.com.JTS.DataAccess.EmployeeRepository;
import eminimki.com.JTS.DataAccess.TaskRepository;
import eminimki.com.JTS.Entities.Employee;
import eminimki.com.JTS.Entities.Task;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskManager implements TaskService {

    private TaskRepository taskRepository;
    private ModelMapperService modelMapperService;
    private EmployeeRepository employeeRepository;
    private TaskBusinessRules taskBusinessRules;
    private EmailManager emailManager;

    @Override
    public List<GetAllTaskResponse> getAll() {
        List<Task> tasks = taskRepository.findAll();
        List<GetAllTaskResponse> taskResponses = tasks.stream()
                .map(task -> this.modelMapperService.forResponse().map(task, GetAllTaskResponse.class))
                .collect(Collectors.toList());

        return taskResponses;
    }

    @Override
    public List<GetAllTaskBySenderIdResponse> getAllTaskBySenderId(int senderId) {
        List<Task> tasks = taskRepository.findBySenderId(senderId);
        List<GetAllTaskBySenderIdResponse> taskResponses = tasks.stream()
                .map(task -> this.modelMapperService.forResponse().map(task, GetAllTaskBySenderIdResponse.class))
                .collect(Collectors.toList());

        return taskResponses;

    }

    @Override
    public List<GetAllTaskByReceiverIdResponse> getAllTaskByReceiverId(int receiverId) {
        List<Task> tasks = taskRepository.findByReceiverId(receiverId);
        List<GetAllTaskByReceiverIdResponse> taskResponses = tasks.stream()
                .map(task -> this.modelMapperService.forResponse().map(task, GetAllTaskByReceiverIdResponse.class))
                .collect(Collectors.toList());

        return taskResponses;
    }

    @Override
    public List<GetTaskStatisticsForSenderResponse> getTaskStatisticsForSender(String sender) {
        List<Object[]> result = taskRepository.getTaskStatisticsForSender(sender);
        List<GetTaskStatisticsForSenderResponse> getTaskStatisticsForSenderResponses = new ArrayList<>();

        for (Object[] row : result) {
            GetTaskStatisticsForSenderResponse getTaskStatisticsForSenderResponse = new GetTaskStatisticsForSenderResponse();
            getTaskStatisticsForSenderResponse.setStatus((String) row[0]);
            getTaskStatisticsForSenderResponse.setCount((Long) row[1]);
            getTaskStatisticsForSenderResponses.add(getTaskStatisticsForSenderResponse);
        }

        return getTaskStatisticsForSenderResponses;
    }

    @Override
    public List<GetTaskStatisticsForReceiverResponse> getTaskStatisticsForReceiver(String receiver) {
        List<Object[]> result = taskRepository.getTaskStatisticsForReceiver(receiver);
        List<GetTaskStatisticsForReceiverResponse> getTaskStatisticsForReceiverResponses = new ArrayList<>();

        for (Object[] row : result) {
            GetTaskStatisticsForReceiverResponse getTaskStatisticsForReceiverResponse = new GetTaskStatisticsForReceiverResponse();
            getTaskStatisticsForReceiverResponse.setStatus((String) row[0]);
            getTaskStatisticsForReceiverResponse.setCount((Long) row[1]);
            getTaskStatisticsForReceiverResponses.add(getTaskStatisticsForReceiverResponse);
        }

        return getTaskStatisticsForReceiverResponses;
    }


    @Override
    public GetTaskByIdResponse getTaskById(int taskId) {
        Task task = this.taskRepository.findById(taskId).orElseThrow();
        GetTaskByIdResponse getTaskByIdResponse =  this.modelMapperService.forResponse().map(task, GetTaskByIdResponse.class);
        return getTaskByIdResponse;
    }

    @Override
    public GetTaskByIdResponse add(CreateTaskRequest createTaskRequest) {
        String currentTime = CommonUtils.getCurrentDateTimeFormatted();

        if (!createTaskRequest.getDate().isEmpty() && !createTaskRequest.getDate().isBlank()){//Create isteğinde bir tarih belirtildi ise geçmiş tarih olamaz , tarih yoksa sorunsuz ekle
            taskBusinessRules.checkDateBefore(createTaskRequest.getDate());
        }


        Task task = this.modelMapperService.forRequest().map(createTaskRequest,Task.class);

        task.setSender(employeeRepository.findByUsername(createTaskRequest.getSender())
                .orElseThrow(() -> new UsernameNotFoundException("Sender not found: " + createTaskRequest.getSender())));
        task.setReceiver(employeeRepository.findByUsername(createTaskRequest.getReceiver())
                .orElseThrow(() -> new UsernameNotFoundException("Receiver not found: " + createTaskRequest.getReceiver())));

        task.setCreatedTime(currentTime);
        task.setModifiedTime(currentTime);

        this.taskRepository.save(task);
        this.emailManager.sendTaskCreateMail(task);
        //this.emailManager.sendMail("eminay122@gmail.com","subject","async testi");

        return this.modelMapperService.forResponse().map(task, GetTaskByIdResponse.class);
    }

    @Override
    public void delete(int id) {
        this.taskRepository.deleteById(id);
    }

    @Override
    public GetTaskByIdResponse update(UpdateTaskRequest updateTaskRequest) {

        if (!updateTaskRequest.getDate().isEmpty()){//Create isteğinde bir tarih belirtildi ise geçmiş tarih olamaz , tarih yoksa sorunsuz ekle
            taskBusinessRules.checkDateBefore(updateTaskRequest.getDate());
        }


        Employee sender = this.employeeRepository.findByUsername(updateTaskRequest.getSender())
                .orElseThrow(() -> new UsernameNotFoundException("Sender not found: " + updateTaskRequest.getSender()));

        Employee receiver = this.employeeRepository.findByUsername(updateTaskRequest.getReceiver())
                .orElseThrow(() -> new UsernameNotFoundException("Receiver not found: " + updateTaskRequest.getReceiver()));

        Task oldTask = taskRepository.getReferenceById(updateTaskRequest.getId());
        Task task = this.modelMapperService.forRequest().map(updateTaskRequest,Task.class);
        task.setReceiver(receiver);
        task.setSender(sender);
        task.setModifiedTime(CommonUtils.getCurrentDateTimeFormatted());
        task.setCreatedTime(oldTask.getCreatedTime());
        this.taskRepository.save(task);

        return this.modelMapperService.forResponse().map(task, GetTaskByIdResponse.class);
    }

    @Override
    public GetTaskByIdResponse updateTaskStatus(UpdateTaskStatusRequest updateTaskStatusRequest) {
        Task task = taskRepository.findById(updateTaskStatusRequest.getId()).orElseThrow();
        task.setStatus(updateTaskStatusRequest.getStatus());
        task.setModifiedTime(CommonUtils.getCurrentDateTimeFormatted());

        this.taskRepository.save(task);

        return this.modelMapperService.forResponse().map(task, GetTaskByIdResponse.class);

    }
}
