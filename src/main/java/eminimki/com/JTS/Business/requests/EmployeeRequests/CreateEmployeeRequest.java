package eminimki.com.JTS.Business.requests.EmployeeRequests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateEmployeeRequest {
    // Employee Entity
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
