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

/**
 * Controller per la gestione dell'autenticazione (Login).
 * Elabora la verifica delle credenziali cifrate con SHA-256, la generazione del Session Token
 * e la sincronizzazione del carrello persistente su database per l'utente loggato.
 */
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

        // Se l'utente è già loggato, reindirizza in base al ruolo
        if (SessionUtil.isUserLoggedIn(request)) {
            if (SessionUtil.isAdmin(request)) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/home");
            }
            return;
        }

        // Altrimenti mostra il form di login e registrazione
        request.getRequestDispatcher("/WEB-INF/view/login-registrazione.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String redirect = request.getParameter("redirect");

        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Email e Password sono campi obbligatori.");
            request.getRequestDispatcher("/WEB-INF/view/login-registrazione.jsp").forward(request, response);
            return;
        }

        try {
            // 1. Cifra la password digitata con SHA-256
            String passwordHash = PasswordUtil.hashPassword(password);

            // 2. Verifica le credenziali nel database
            Utente utente = utenteDAO.doRetrieveByEmailAndPassword(email, passwordHash);

            if (utente != null) {
                // 3. Login riuscito: registra l'utente in sessione e genera il sessionToken
                SessionUtil.login(request, utente);

                // 4. Sincronizzazione del carrello (unione carrello di sessione con carrello su DB)
                Carrello sessionCart = SessionUtil.getCart(request);
                Carrello dbCart = carrelloDAO.doRetrieveByUtente(utente.getId());

                if (dbCart != null && !dbCart.isEmpty()) {
                    for (RigaCarrello rigaDB : dbCart.getRighe()) {
                        sessionCart.aggiungiProdotto(rigaDB.getProdotto(), rigaDB.getQuantita());
                    }
                }
                // Salva lo stato unificato del carrello su database
                carrelloDAO.doSaveOrUpdate(utente.getId(), sessionCart);

                // 5. Reindirizzamento in base a provenienza o ruolo
                if (redirect != null && !redirect.trim().isEmpty() && !redirect.contains("login")) {
                    response.sendRedirect(request.getContextPath() + redirect);
                } else if (utente.isAdmin()) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/home");
                }

            } else {
                // Credenziali errate
                request.setAttribute("errorMessage", "Credenziali non valide. Verifica l'indirizzo email o la password inserita.");
                request.getRequestDispatcher("/WEB-INF/view/login-registrazione.jsp").forward(request, response);
            }

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Si è verificato un errore di connessione durante l'autenticazione.");
            request.getRequestDispatcher("/WEB-INF/view/login-registrazione.jsp").forward(request, response);
        }
    }
}
