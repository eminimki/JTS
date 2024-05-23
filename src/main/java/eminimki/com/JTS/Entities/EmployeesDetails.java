package eminimki.com.JTS.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "employeeDetails")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeesDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "name")
    private String name ;

    @Column(name = "lastname")
    private String lastname ;

    @Column(name ="gender", columnDefinition = "varchar(7) default 'Male'")
    private String gender;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "position")
    private String position;

    @Column(name = "created_time", nullable = false)
    private String createdTime;

    @Column(name = "modified_time")
    private String modifiedTime;
}
