package eminimki.com.JTS.WebAPI.Controllers;

import eminimki.com.JTS.Business.Services.TaskService;
import eminimki.com.JTS.Business.requests.TaskRequests.CreateTaskRequest;
import eminimki.com.JTS.Business.requests.TaskRequests.UpdateTaskRequest;
import eminimki.com.JTS.Business.requests.TaskRequests.UpdateTaskStatusRequest;
import eminimki.com.JTS.Business.responses.TaskResponses.*;
import eminimki.com.JTS.DataAccess.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskControllers {
    private TaskService taskService;
    private TaskRepository taskRepository;

    @GetMapping("/get/all")
    public List<GetAllTaskResponse> getAll(){return taskService.getAll();}

    @GetMapping("/get/sender/{senderId}")
    public List<GetAllTaskBySenderIdResponse> getAllTaskBySenderIdResponse(@PathVariable int senderId){
        return taskService.getAllTaskBySenderId(senderId);
    }

    @GetMapping("/get/receiver/{receiverId}")
    public List<GetAllTaskByReceiverIdResponse> getAllTaskByReceiverIdResponse(@PathVariable int receiverId){
        return taskService.getAllTaskByReceiverId(receiverId);
    }

    @GetMapping("/get/{taskId}")
    public GetTaskByIdResponse getTaskById(@PathVariable int taskId){
        return taskService.getTaskById(taskId);
    }

    @GetMapping("/get/statistics/sender/{sender}")
    public List<GetTaskStatisticsForSenderResponse> getTaskStatisticsForSender(@PathVariable String sender) {
        return taskService.getTaskStatisticsForSender(sender);
    }

    @GetMapping("/get/statistics/receiver/{receiver}")
    public List<GetTaskStatisticsForReceiverResponse> getTaskStatisticsForReceiver(@PathVariable String receiver) {
        return taskService.getTaskStatisticsForReceiver(receiver);
    }

    @PostMapping("/post/add")
    public ResponseEntity<GetTaskByIdResponse> add(@RequestBody CreateTaskRequest createTaskRequest){
        return ResponseEntity.ok().body(this.taskService.add(createTaskRequest));
    }


    @DeleteMapping("/delete/{taskId}")
    public void delete(@PathVariable int taskId ){
        this.taskService.delete(taskId);
    }

    @PutMapping("/put/update")
    public ResponseEntity<GetTaskByIdResponse> update(@RequestBody UpdateTaskRequest updateTaskRequest){
        return ResponseEntity.ok().body(this.taskService.update(updateTaskRequest));
    }

    @PutMapping("/put/update/status")
    public ResponseEntity<GetTaskByIdResponse> updateTaskStatus(@RequestBody UpdateTaskStatusRequest updateTaskStatusRequest){

        return ResponseEntity.ok().body(this.taskService.updateTaskStatus(updateTaskStatusRequest));
    }
}
