package CTLWorkshop.master;

import lombok.Data;
import javax.persistence.*;
import java.util.List;

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

    public Boolean checkLogin(String u, String p){
        return u.equals(username) && p.equals(password);
    }
}
