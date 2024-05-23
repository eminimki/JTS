package eminimki.com.JTS.Business.responses.TaskResponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllTaskBySenderIdResponse {

    private int id;
    private int sender;
    private int receiver;
    private String name;
    private String description;
    private String status;
    private String date;

}
