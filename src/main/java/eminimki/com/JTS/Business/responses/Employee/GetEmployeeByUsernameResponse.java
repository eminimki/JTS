package eminimki.com.JTS.Business.responses.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetEmployeeByUsernameResponse {

    // Employee Entity
    private int id;
    private String username;
    private String mail ;
    private String password ;
    private String role ;

    // Employee Details Entity
    private String name ;
    private String lastname ;
    private String gender ;
    private String address;
    private String phone;
    private String mobile;
    private String position;
}
