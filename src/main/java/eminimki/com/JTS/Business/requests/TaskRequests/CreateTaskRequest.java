package eminimki.com.JTS.Business.requests.TaskRequests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTaskRequest {

    private String sender ;
    private String receiver ;
    private String name ;
    private String description ;
    private String status ;
    private String date ;


}