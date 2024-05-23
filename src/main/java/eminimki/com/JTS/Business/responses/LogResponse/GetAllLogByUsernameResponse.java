package eminimki.com.JTS.Business.responses.LogResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllLogByUsernameResponse {

    private int id;
    private String actioner;
    private String description;
    private String time;
}
