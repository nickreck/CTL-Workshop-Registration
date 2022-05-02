package CTLWorkshop.master;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

    @Bean
    public PasswordEncoder getPasswordEncoder()
    {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.inMemoryAuthentication()
                //username and password for login in are currently hard coded here with ".with user" = username and ".password" = password
                .withUser("user1")
                .password("password1")
                .roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
        http.authorizeRequests()
                .antMatchers("/","/admin", "/adminsubmitted", "/attendance","/attendancecollege","/attendancedepartment","/attendancedisplay","/attendanceexport","/editattendance","/attendancesubmission","/attendancesubmitted","/workshopedit","/workshopedit_submitted","/deleteworkshop","/deleteworkshop_submitted").hasRole("ADMIN")
                .antMatchers("/registration","/registrationsubmitted").permitAll()
                .and()
                .formLogin()
                .loginPage("/adminLogin")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }


}