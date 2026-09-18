package it.unisa.diatech.control;

import it.unisa.diatech.dao.UtenteDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.naming.NamingException;

@WebServlet(name = "CheckEmailServlet", urlPatterns = {"/check-email"})
public class CheckEmailServlet extends HttpServlet {

    private UtenteDAO utenteDAO;

    @Override
    public void init() throws ServletException {
        this.utenteDAO = new UtenteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String email = request.getParameter("email");

        if (email == null || email.trim().isEmpty()) {
            out.print("{\"exists\":false}");
            out.flush();
            return;
        }

        try {
            boolean exists = utenteDAO.checkEmailExists(email.trim().toLowerCase());
            out.print("{\"exists\":" + exists + "}");
            out.flush();

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            out.print("{\"exists\":false,\"error\":true}");
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
