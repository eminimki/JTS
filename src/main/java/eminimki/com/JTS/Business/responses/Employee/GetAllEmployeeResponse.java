package eminimki.com.JTS.Business.responses.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllEmployeeResponse {

    private int id;
    private String username ;
    private String name ;
    private String lastname ;
    private String mail ;
    private String password ;
    private String role;

}
