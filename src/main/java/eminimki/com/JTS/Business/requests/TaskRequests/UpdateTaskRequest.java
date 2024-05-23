package eminimki.com.JTS.Business.requests.TaskRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequest {

    private int id;
    private String sender;
    private String receiver;
    private String name;
    private String description;
    private String status;
    private String date;
}
