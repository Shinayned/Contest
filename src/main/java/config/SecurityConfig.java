package config;

import component.CustomAuthFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    @Order(1)
    public static class AdminConfig extends WebSecurityConfigurerAdapter {
        @Bean
        protected UserDetailsService userDetailsService() {
            InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
            manager.createUser(User
                    .withUsername("admin")
                    .password(passwordEncoder().encode("adminPass"))
                    .roles("ADMIN")
                    .build());
            return manager;
        }

        @Bean(name = "Other")
        protected DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(userDetailsService());
            authProvider.setPasswordEncoder(passwordEncoder());
            return authProvider;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/admin*")
                    .authorizeRequests()
                    .anyRequest()
                    .hasRole("ADMIN")

                    .and()
                    .formLogin()
                    .loginPage("/loginAdmin")
                    .loginProcessingUrl("/loginAdmin")
                    .failureUrl("/loginAdmin?error=loginError")
                    .defaultSuccessUrl("/adminPage")
                    .usernameParameter("login")
                    .passwordParameter("password")

                    .and()
                    .logout()
                    .logoutUrl("/admin_logout")
                    .logoutSuccessUrl("/protectedLinks")
                    .deleteCookies("JSESSIONID")

                    .and()
                    .exceptionHandling()
                    .accessDeniedPage("/403")

                    .and()
                    .csrf().disable();
        }
    }

    @Configuration
    @Order(2)
    public class ParticipantConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        private UserDetailsServiceImpl userDetailsService;

        @Autowired
        private CustomAuthFailureHandler customAuthFailureHandler;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/")
                    .hasRole("PARTICIPANT")

                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/home", false)
                    .failureHandler(customAuthFailureHandler)
                    .usernameParameter("email")
                    .passwordParameter("password")

                    .and()
                    .logout()
                    .logoutSuccessUrl("/")
                    .deleteCookies("JSESSIONID")

                    .and()
                    .rememberMe()
                    .userDetailsService(userDetailsService)

                    .and()
                    .csrf().disable();
        }

        @Bean
        protected DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(userDetailsService);
            authProvider.setPasswordEncoder(passwordEncoder());
            return authProvider;
        }
    }
}
