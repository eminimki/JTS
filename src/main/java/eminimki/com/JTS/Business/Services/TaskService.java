package eminimki.com.JTS.Business.Services;

import eminimki.com.JTS.Business.requests.TaskRequests.CreateTaskRequest;
import eminimki.com.JTS.Business.requests.TaskRequests.UpdateTaskRequest;
import eminimki.com.JTS.Business.requests.TaskRequests.UpdateTaskStatusRequest;
import eminimki.com.JTS.Business.responses.TaskResponses.*;

import java.util.List;

public interface TaskService {

    List<GetAllTaskResponse> getAll();
    List<GetAllTaskBySenderIdResponse> getAllTaskBySenderId(int senderId);
    List<GetAllTaskByReceiverIdResponse> getAllTaskByReceiverId(int receiverId);
    List<GetTaskStatisticsForSenderResponse> getTaskStatisticsForSender(String sender);
    List<GetTaskStatisticsForReceiverResponse> getTaskStatisticsForReceiver(String receiver);
    GetTaskByIdResponse getTaskById(int id);

    GetTaskByIdResponse add(CreateTaskRequest createTaskRequest);
    void delete(int id);
    GetTaskByIdResponse update(UpdateTaskRequest updateTaskRequest);
    GetTaskByIdResponse updateTaskStatus(UpdateTaskStatusRequest updateTaskStatusRequest);



}
