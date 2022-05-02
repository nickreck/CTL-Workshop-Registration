package CTLWorkshop.master;

import lombok.Data;
import javax.persistence.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

@Data
@Entity
// Table name within schema
@Table(name = "appform")
public class  Attendee
{
    //The attendee object has several properties with all required information about a participant in a workshop
    // id is the attendee's email
    @Id
    @Column(nullable = false)
    private String id;
    @Column(nullable = false, unique = false)
    private String email;
    @Column(nullable = false, unique = false)
    private String firstname;
    @Column(nullable = false)
    private String lastname;
    @Column(nullable = false)
    private String department;
    @Column(nullable = false)
    private String college;
    @Column(nullable = false)
    private String position;
    // workshopnum is used to connect the attendees to their workshop in the database
    @Column(nullable = false)
    private int workshopnum;
    @Column(nullable = true)
    private String attendance;

    //email code
    public static void send(String from,String password,String to,String sub,String msg)
    {
        //Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator()
                {
                    protected PasswordAuthentication getPasswordAuthentication()
                    {
                        return new PasswordAuthentication(from,password);
                    }
                });
        //compose message
        try
        {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject(sub);
            message.setText(msg);
            //send message
            Transport.send(message);
        }
        catch (MessagingException e) {throw new RuntimeException(e);}

    }
}


