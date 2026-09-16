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

@WebServlet(name = "ProdottoServlet", urlPatterns = {"/prodotto"})
public class ProdottoServlet extends HttpServlet {

    private ProdottoDAO prodottoDAO;

    @Override
    public void init() throws ServletException {
        this.prodottoDAO = new ProdottoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/catalogo");
            return;
        }

        try {
            int id = Integer.parseInt(idParam.trim());
            Prodotto prodotto = prodottoDAO.doRetrieveByKey(id);

            if (prodotto != null && !prodotto.isCancellato()) {
                List<Prodotto> compatibili = prodottoDAO.doRetrieveCompatibili(id);
                request.setAttribute("prodotto", prodotto);
                request.setAttribute("prodottiCompatibili", compatibili);
            } else {
                request.setAttribute("prodotto", null);
            }

            request.getRequestDispatcher("/WEB-INF/view/dettaglio-prodotto.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/catalogo");
        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore dettaglio prodotto");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
