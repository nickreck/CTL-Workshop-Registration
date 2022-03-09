package CTLWorkshop.master;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "login")
public class Login
{
    @Id
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
}
