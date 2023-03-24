import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String USERNAME = "CSisCoolStuff";
    private static final String PASSWORD = "CSisCoolStuff";
    private static final String USERNAME_COOKIE = "username";
    private static final String PASSWORD_COOKIE = "password";
    private static final int COOKIE_MAX_AGE = 6 * 30 * 24 * 60 * 60; // 6 months in seconds

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is already logged in using cookies
        String username = null;
        String password = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(USERNAME_COOKIE)) {
                    username = cookie.getValue();
                } else if (cookie.getName().equals(PASSWORD_COOKIE)) {
                    password = cookie.getValue();
                }
            }
        }

        // If both username and password are found in cookies, show "Welcome Back" page
        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>Welcome Back</h1>");
            out.println("</body></html>");
            return;
        }

            // Otherwise, show login form
    boolean autoLogin = false;
    String autoLoginChecked = "";
    if (cookies == null) {
        // If there are no cookies, this is the first time the user is visiting the page
        autoLogin = true;
        autoLoginChecked = "checked";
    } else {
        // If there are cookies but they don't match the expected values, clear them
        if (!USERNAME.equals(username) || !PASSWORD.equals(password)) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        } else {
            autoLogin = true;
            autoLoginChecked = "checked";
        }
    }

    // Write the login form
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<html><body>");
    out.println("<h1>Login</h1>");
    out.println("<form method='post'>");
    out.println("Username: <input type='text' name='username' value='" + username + "'><br>");
    out.println("Password: <input type='password' name='password' value='" + password + "'><br>");
    out.println("<input type='checkbox' name='autologin' value='true' " + autoLoginChecked + ">Auto-login<br>");
    out.println("<input type='submit' value='Login'>");
    out.println("</form>");
    out.println("</body></html>");

}

@Override
public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    // Get the username and password from the form
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String autoLogin = request.getParameter("autologin");

    // If the username and password are correct, create cookies for auto-login and show welcome page
    if (USERNAME.equals(username) && PASSWORD.equals(password)) {
        if (autoLogin != null && autoLogin.equals("true")) {
            Cookie usernameCookie = new Cookie(USERNAME_COOKIE, USERNAME);
            usernameCookie.setMaxAge(COOKIE_MAX_AGE);
            response.addCookie(usernameCookie);
            Cookie passwordCookie = new Cookie(PASSWORD_COOKIE, PASSWORD);
            passwordCookie.setMaxAge(COOKIE_MAX_AGE);
            response.addCookie(passwordCookie);
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Welcome</h1>");
        out.println("</body></html>");
    } else {
// If the username and password are incorrect, show error message and login form again
response.setContentType("text/html");
PrintWriter out = response.getWriter();
out.println("<html><body>");
out.println("<h1>Error</h1>");
out.println("<p>Incorrect username or password.</p>");
out.println("<form method='post'>");
out.println("Username: <input type='text' name='username'><br>");
out.println("Password: <input type='password' name='password'><br>");
out.println("<input type='checkbox' name='autologin' value='true'>Auto-login<br>");
out.println("<input type='submit' value='Login'>");
out.println("</form>");
out.println("</body></html>");
}
}

}
