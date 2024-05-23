package eminimki.com.JTS.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "logs")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JTSLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "description")
    private String description;

    @Column(name = "actioner")
    private String actioner;

    @Column(name = "time")
    private String time;

    @Column(name = "created_time", nullable = false)
    private String createdTime;

    @Column(name = "modified_time")
    private String modifiedTime;

}
