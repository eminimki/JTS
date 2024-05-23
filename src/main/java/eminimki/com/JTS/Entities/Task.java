package eminimki.com.JTS.Entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "Tasks")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender",nullable = false)
    private Employee sender;

    @ManyToOne
    @JoinColumn(name = "receiver",nullable = false)
    private Employee receiver;

    @Column(name = "name")
    private String name;

    @Column(name = "description" , length = 2047)
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "date")
    private String date;

    @Column(name = "created_time", nullable = false)
    private String createdTime;

    @Column(name = "modified_time")
    private String modifiedTime;
}
