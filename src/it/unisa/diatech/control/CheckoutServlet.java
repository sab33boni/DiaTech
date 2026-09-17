package it.unisa.diatech.control;

import it.unisa.diatech.dao.CarrelloDAO;
import it.unisa.diatech.dao.OrdineDAO;
import it.unisa.diatech.model.Carrello;
import it.unisa.diatech.model.Ordine;
import it.unisa.diatech.model.RigaCarrello;
import it.unisa.diatech.model.RigaOrdine;
import it.unisa.diatech.model.Utente;
import it.unisa.diatech.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private OrdineDAO ordineDAO;
    private CarrelloDAO carrelloDAO;

    @Override
    public void init() throws ServletException {
        this.ordineDAO = new OrdineDAO();
        this.carrelloDAO = new CarrelloDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Utente user = SessionUtil.getLoggedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/checkout");
            return;
        }

        Carrello carrello = SessionUtil.getCart(request);
        if (carrello == null || carrello.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/carrello");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/view/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Utente user = SessionUtil.getLoggedUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/checkout");
            return;
        }

        String token = request.getParameter("sessionToken");
        if (!SessionUtil.isValidToken(request, token)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Sessione non valida o scaduta (Controllo di sicurezza).");
            return;
        }

        Carrello carrello = SessionUtil.getCart(request);
        if (carrello == null || carrello.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/carrello");
            return;
        }

        String indirizzo = request.getParameter("indirizzo");
        String citta = request.getParameter("citta");
        String cap = request.getParameter("cap");
        String telefono = request.getParameter("telefono");
        String metodoPagamento = request.getParameter("metodoPagamento");

        if (indirizzo == null || citta == null || cap == null || indirizzo.trim().isEmpty() || citta.trim().isEmpty() || cap.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Tutti i campi di spedizione sono obbligatori.");
            request.getRequestDispatcher("/WEB-INF/view/checkout.jsp").forward(request, response);
            return;
        }

        try {
            Ordine ordine = new Ordine();
            ordine.setIdUtente(user.getId());
            ordine.setTotale(carrello.getTotale());
            ordine.setStato("IN_LAVORAZIONE");
            ordine.setIndirizzoSpedizione(indirizzo.trim());
            ordine.setCitta(citta.trim());
            ordine.setCap(cap.trim());
            ordine.setMetodoPagamento(metodoPagamento != null ? metodoPagamento : "Carta di Credito");

            List<RigaOrdine> righeOrdine = new ArrayList<>();
            for (RigaCarrello rc : carrello.getRighe()) {
                RigaOrdine ro = new RigaOrdine();
                ro.setProdotto(rc.getProdotto());
                ro.setQuantita(rc.getQuantita());
                ro.setPrezzoUnitario(rc.getProdotto().getPrezzo());
                righeOrdine.add(ro);
            }
            ordine.setRighe(righeOrdine);

            int idOrdineGenerato = ordineDAO.doSave(ordine);
            ordine.setId(idOrdineGenerato);

            carrello.svuota();
            carrelloDAO.doDeleteByUtente(user.getId());

            request.setAttribute("ordine", ordine);
            request.getRequestDispatcher("/WEB-INF/view/conferma-ordine.jsp").forward(request, response);

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante il completamento dell'ordine: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/view/checkout.jsp").forward(request, response);
        }
    }
}
