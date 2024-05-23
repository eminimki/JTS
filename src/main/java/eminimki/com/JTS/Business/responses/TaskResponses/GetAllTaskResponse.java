package eminimki.com.JTS.Business.responses.TaskResponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllTaskResponse {

    private int id;
    private int senderId;
    private String senderUsername;
    private int receiverId;
    private String receiverUsername;
    private String name;
    private String description;
    private String status;
    private String date;

}
