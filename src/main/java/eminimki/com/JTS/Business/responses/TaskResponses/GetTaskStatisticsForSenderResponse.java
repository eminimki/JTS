package eminimki.com.JTS.Business.responses.TaskResponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTaskStatisticsForSenderResponse {

    private String status;
    private Long count;

}
