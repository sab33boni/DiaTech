package it.unisa.diatech.control;

import it.unisa.diatech.dao.ProdottoDAO;
import it.unisa.diatech.model.Prodotto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;

/**
 * Controller per la Home Page (/home).
 * Recupera i prodotti dal database tramite ProdottoDAO e inoltra la richiesta
 * alla vista protetta /WEB-INF/view/home.jsp nel rispetto dell'architettura MVC.
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    private ProdottoDAO prodottoDAO;

    @Override
    public void init() throws ServletException {
        this.prodottoDAO = new ProdottoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Recupera i primi prodotti attivi per popolare la sezione "Prodotti in Evidenza"
            List<Prodotto> prodotti = prodottoDAO.doRetrieveAll("id_asc");
            
            // Seleziona i primi 6 prodotti per la vetrina della home page
            List<Prodotto> inEvidenza = prodotti.size() > 6 ? prodotti.subList(0, 6) : prodotti;
            
            request.setAttribute("prodottiInEvidenza", inEvidenza);
            
            // Forward verso la vista protetta
            request.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(request, response);

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel caricamento della Home Page.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
