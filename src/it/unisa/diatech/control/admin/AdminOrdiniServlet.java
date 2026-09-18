package it.unisa.diatech.control.admin;

import it.unisa.diatech.dao.OrdineDAO;
import it.unisa.diatech.dao.UtenteDAO;
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

@WebServlet(name = "AdminOrdiniServlet", urlPatterns = {"/admin/ordini"})
public class AdminOrdiniServlet extends HttpServlet {

    private OrdineDAO ordineDAO;
    private UtenteDAO utenteDAO;

    @Override
    public void init() throws ServletException {
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

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String idClienteParam = request.getParameter("idCliente");

        Integer idCliente = null;
        if (idClienteParam != null && !idClienteParam.trim().isEmpty()) {
            try {
                idCliente = Integer.parseInt(idClienteParam.trim());
            } catch (NumberFormatException ignored) {}
        }

        try {
            List<Utente> clienti = utenteDAO.doRetrieveAllClienti();
            List<Ordine> ordini = ordineDAO.doRetrieveByFiltri(startDate, endDate, idCliente);

            request.setAttribute("clienti", clienti);
            request.setAttribute("ordini", ordini);

            if ("status_updated".equals(request.getParameter("success"))) {
                request.setAttribute("successMessage", "Stato dell'ordine aggiornato con successo.");
            }

            request.getRequestDispatcher("/WEB-INF/view/admin/gestione-ordini.jsp").forward(request, response);

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nella consultazione ordini admin.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SessionUtil.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login?error=unauthorized_admin");
            return;
        }

        String action = request.getParameter("action");
        String idOrdineParam = request.getParameter("idOrdine");
        String nuovoStato = request.getParameter("nuovoStato");

        try {
            if ("updateStatus".equalsIgnoreCase(action) && idOrdineParam != null && nuovoStato != null) {
                int idOrdine = Integer.parseInt(idOrdineParam.trim());
                ordineDAO.doUpdateStato(idOrdine, nuovoStato);
                response.sendRedirect(request.getContextPath() + "/admin/ordini?success=status_updated");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/admin/ordini");

        } catch (SQLException | NamingException | NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/ordini?error=update_failed");
        }
    }
}
