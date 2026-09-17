package it.unisa.diatech.control;

import it.unisa.diatech.dao.OrdineDAO;
import it.unisa.diatech.model.Ordine;
import it.unisa.diatech.model.Utente;
import it.unisa.diatech.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;

@WebServlet(name = "OrdiniUtenteServlet", urlPatterns = {"/ordini"})
public class OrdiniUtenteServlet extends HttpServlet {

    private OrdineDAO ordineDAO;

    @Override
    public void init() throws ServletException {
        this.ordineDAO = new OrdineDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Utente user = SessionUtil.getLoggedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/ordini");
            return;
        }

        try {
            List<Ordine> ordini = ordineDAO.doRetrieveByUtente(user.getId());
            request.setAttribute("ordini", ordini);
            request.getRequestDispatcher("/WEB-INF/view/miei-ordini.jsp").forward(request, response);

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore durante il recupero dello storico ordini.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
