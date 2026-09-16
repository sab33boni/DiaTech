package it.unisa.diatech.control;

import it.unisa.diatech.dao.UtenteDAO;
import it.unisa.diatech.model.Utente;
import it.unisa.diatech.util.PasswordUtil;
import it.unisa.diatech.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import javax.naming.NamingException;

@WebServlet(name = "RegistrazioneServlet", urlPatterns = {"/registrazione"})
public class RegistrazioneServlet extends HttpServlet {

    private UtenteDAO utenteDAO;

    @Override
    public void init() throws ServletException {
        this.utenteDAO = new UtenteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/login");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confermaPassword = request.getParameter("confermaPassword");
        String indirizzo = request.getParameter("indirizzo");
        String citta = request.getParameter("citta");
        String cap = request.getParameter("cap");
        String telefono = request.getParameter("telefono");

        if (nome == null || cognome == null || email == null || password == null || confermaPassword == null
                || nome.trim().isEmpty() || cognome.trim().isEmpty() || email.trim().isEmpty()
                || password.trim().isEmpty() || confermaPassword.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Tutti i campi contrassegnati con * sono obbligatori.");
            request.getRequestDispatcher("/WEB-INF/view/login-registrazione.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confermaPassword)) {
            request.setAttribute("errorMessage", "Le password inserite non coincidono.");
            request.getRequestDispatcher("/WEB-INF/view/login-registrazione.jsp").forward(request, response);
            return;
        }

        if (password.length() < 8) {
            request.setAttribute("errorMessage", "La password deve contenere almeno 8 caratteri.");
            request.getRequestDispatcher("/WEB-INF/view/login-registrazione.jsp").forward(request, response);
            return;
        }

        try {
            if (utenteDAO.checkEmailExists(email)) {
                request.setAttribute("errorMessage", "L'indirizzo email inserito è già associato ad un account.");
                request.getRequestDispatcher("/WEB-INF/view/login-registrazione.jsp").forward(request, response);
                return;
            }

            Utente utente = new Utente();
            utente.setNome(nome.trim());
            utente.setCognome(cognome.trim());
            utente.setEmail(email.trim().toLowerCase());
            utente.setPasswordHash(PasswordUtil.hashPassword(password));
            utente.setIndirizzo(indirizzo != null ? indirizzo.trim() : "");
            utente.setCitta(citta != null ? citta.trim() : "");
            utente.setCap(cap != null ? cap.trim() : "");
            utente.setTelefono(telefono != null ? telefono.trim() : "");
            utente.setRuolo("REGISTRATO");

            int id = utenteDAO.doSave(utente);
            utente.setId(id);

            SessionUtil.login(request, utente);
            response.sendRedirect(request.getContextPath() + "/home?registered=true");

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante la registrazione.");
            request.getRequestDispatcher("/WEB-INF/view/login-registrazione.jsp").forward(request, response);
        }
    }
}
