package eminimki.com.JTS.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Table(name = "employees")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username", unique = true , nullable = false)
    private String username ;


    @Column(name = "mail", unique = true, nullable = false)
    private String mail ;

    @Column(name = "password")
    private String password ;

    @Column(name = "role")
    private String role;

    @Column(name = "created_time", nullable = false )
    private String createdTime;

    @Column(name = "modified_time")
    private String modifiedTime;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private EmployeesDetails employeesDetails;

    @OneToMany(mappedBy = "receiver")
    private Set<Task> tasks;




}
