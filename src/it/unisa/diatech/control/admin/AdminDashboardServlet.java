package it.unisa.diatech.control.admin;

import it.unisa.diatech.dao.OrdineDAO;
import it.unisa.diatech.dao.ProdottoDAO;
import it.unisa.diatech.dao.UtenteDAO;
import it.unisa.diatech.model.Ordine;
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

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    private ProdottoDAO prodottoDAO;
    private OrdineDAO ordineDAO;
    private UtenteDAO utenteDAO;

    @Override
    public void init() throws ServletException {
        this.prodottoDAO = new ProdottoDAO();
        this.ordineDAO = new OrdineDAO();
        this.utenteDAO = new UtenteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SessionUtil.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login?error=unauthorized_admin");
            return;
        }

        try {
            int totaleProdotti = prodottoDAO.doRetrieveAllAdmin().size();
            int totaleClienti = utenteDAO.doRetrieveAllClienti().size();
            List<Ordine> ordini = ordineDAO.doRetrieveByFiltri(null, null, null);
            int totaleOrdini = ordini.size();

            double volumeVendite = 0.0;
            for (Ordine o : ordini) {
                if (!"ANNULLATO".equalsIgnoreCase(o.getStato())) {
                    volumeVendite += o.getTotale();
                }
            }

            List<Ordine> ordiniRecenti = ordini.size() > 5 ? ordini.subList(0, 5) : ordini;

            request.setAttribute("totaleProdotti", totaleProdotti);
            request.setAttribute("totaleClienti", totaleClienti);
            request.setAttribute("totaleOrdini", totaleOrdini);
            request.setAttribute("volumeVendite", volumeVendite);
            request.setAttribute("ordiniRecenti", ordiniRecenti);

            request.getRequestDispatcher("/WEB-INF/view/admin/dashboard.jsp").forward(request, response);

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore caricamento Dashboard Admin.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
