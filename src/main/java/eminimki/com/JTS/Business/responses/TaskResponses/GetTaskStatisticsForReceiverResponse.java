package eminimki.com.JTS.Business.responses.TaskResponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTaskStatisticsForReceiverResponse {

    private String status;
    private Long count;

}
