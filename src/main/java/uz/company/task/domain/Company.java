package uz.company.task.domain;

import lombok.Data;
import uz.company.task.domain.enums.Status;

import javax.persistence.Entity;
import javax.persistence.*;

@Data
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser user;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_address")
    private String companyAddress;

    @Column(name = "company_zip_code")
    private String companyZipCode;

    @Column(name = "status")
    private Status status;

    @Column(name = "activation_code")
    private String activationCode;

}
