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
            List<Prodotto> prodotti = prodottoDAO.doRetrieveAll("id_asc");
            List<Prodotto> inEvidenza = prodotti.size() > 6 ? prodotti.subList(0, 6) : prodotti;
            request.setAttribute("prodottiInEvidenza", inEvidenza);
            request.getRequestDispatcher("/WEB-INF/view/home.jsp").forward(request, response);
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore Home Page");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
