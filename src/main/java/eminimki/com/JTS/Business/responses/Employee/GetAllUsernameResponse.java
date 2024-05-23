package eminimki.com.JTS.Business.responses.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllUsernameResponse {
    private int id;
    private String username ;
}
