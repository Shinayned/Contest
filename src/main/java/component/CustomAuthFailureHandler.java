package component;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String email = request.getParameter("email");
        StringBuilder errorUrl = new StringBuilder("/login.html?error=");

        if(exception instanceof BadCredentialsException) {
            errorUrl.append("token");
        } else if(exception instanceof DisabledException) {
            errorUrl.append("disabled");
        } else {
            errorUrl.append("true");
        }

        if(!email.isEmpty()) {
            errorUrl.append("&email=" + email);
        }

        getRedirectStrategy().sendRedirect(request, response, errorUrl.toString());
    }
}