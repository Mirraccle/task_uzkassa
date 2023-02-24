package uz.company.task.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser user;

    @Column(name = "company_name")
    private String firstName;

    @Column(name = "company_address")
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;
}
