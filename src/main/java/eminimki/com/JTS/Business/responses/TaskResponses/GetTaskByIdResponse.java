package eminimki.com.JTS.Business.responses.TaskResponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTaskByIdResponse {

    private int id;
    private String senderUsername;
    private String receiverUsername;
    private String name;
    private String description;
    private String status;
    private String date;
}
