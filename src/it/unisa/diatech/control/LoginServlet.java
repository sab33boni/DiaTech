package it.unisa.diatech.control;

import it.unisa.diatech.dao.CarrelloDAO;
import it.unisa.diatech.dao.UtenteDAO;
import it.unisa.diatech.model.Carrello;
import it.unisa.diatech.model.RigaCarrello;
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

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private UtenteDAO utenteDAO;
    private CarrelloDAO carrelloDAO;

    @Override
    public void init() throws ServletException {
        this.utenteDAO = new UtenteDAO();
        this.carrelloDAO = new CarrelloDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (SessionUtil.isUserLoggedIn(request)) {
            if (SessionUtil.isAdmin(request)) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
            return;
        }

        request.getRequestDispatcher("/WEB-INF/view/login-registrazione.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String redirect = request.getParameter("redirect");

        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Email e Password sono obbligatorie.");
            request.getRequestDispatcher("/WEB-INF/view/login-registrazione.jsp").forward(request, response);
            return;
        }

        try {
            String passwordHash = PasswordUtil.hashPassword(password);
            Utente utente = utenteDAO.doRetrieveByEmailAndPassword(email, passwordHash);

            if (utente != null) {
                SessionUtil.login(request, utente);

                Carrello sessionCart = SessionUtil.getCart(request);
                Carrello dbCart = carrelloDAO.doRetrieveByUtente(utente.getId());

                if (dbCart != null && !dbCart.isEmpty()) {
                    for (RigaCarrello rigaDB : dbCart.getRighe()) {
                        sessionCart.aggiungiProdotto(rigaDB.getProdotto(), rigaDB.getQuantita());
                    }
                }
                carrelloDAO.doSaveOrUpdate(utente.getId(), sessionCart);

                if (redirect != null && !redirect.trim().isEmpty() && !redirect.contains("login")) {
                    response.sendRedirect(request.getContextPath() + redirect);
                } else if (utente.isAdmin()) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/home");
                }

            } else {
                request.setAttribute("errorMessage", "Credenziali non valide.");
                request.getRequestDispatcher("/WEB-INF/view/login-registrazione.jsp").forward(request, response);
            }

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante l'autenticazione.");
            request.getRequestDispatcher("/WEB-INF/view/login-registrazione.jsp").forward(request, response);
        }
    }
}
