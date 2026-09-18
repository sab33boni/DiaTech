package it.unisa.diatech.control;

import it.unisa.diatech.dao.CarrelloDAO;
import it.unisa.diatech.dao.ProdottoDAO;
import it.unisa.diatech.model.Carrello;
import it.unisa.diatech.model.Prodotto;
import it.unisa.diatech.model.RigaCarrello;
import it.unisa.diatech.model.Utente;
import it.unisa.diatech.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Locale;
import javax.naming.NamingException;

@WebServlet(name = "CarrelloAjaxServlet", urlPatterns = {"/carrello-ajax"})
public class CarrelloAjaxServlet extends HttpServlet {

    private ProdottoDAO prodottoDAO;
    private CarrelloDAO carrelloDAO;

    @Override
    public void init() throws ServletException {
        this.prodottoDAO = new ProdottoDAO();
        this.carrelloDAO = new CarrelloDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        String idParam = request.getParameter("idProdotto");
        String qtyParam = request.getParameter("quantita");

        Carrello carrello = SessionUtil.getCart(request);
        Utente loggedUser = SessionUtil.getLoggedUser(request);

        boolean success = false;
        String message = "Operazione fallita";
        double rowSubtotal = 0.0;

        try {
            if ("add".equalsIgnoreCase(action) && idParam != null) {
                int id = Integer.parseInt(idParam.trim());
                int quantita = qtyParam != null ? Math.max(1, Integer.parseInt(qtyParam.trim())) : 1;

                Prodotto p = prodottoDAO.doRetrieveByKey(id);
                if (p != null && !p.isCancellato()) {
                    carrello.aggiungiProdotto(p, quantita);
                    success = true;
                    message = p.getNome() + " aggiunto al carrello.";
                }
            } else if ("update".equalsIgnoreCase(action) && idParam != null && qtyParam != null) {
                int id = Integer.parseInt(idParam.trim());
                int quantita = Integer.parseInt(qtyParam.trim());

                if (quantita <= 0) {
                    carrello.rimuoviProdotto(id);
                    success = true;
                    message = "Articolo rimosso.";
                } else {
                    for (RigaCarrello r : carrello.getRighe()) {
                        if (r.getProdotto().getId() == id) {
                            r.setQuantita(Math.min(quantita, r.getProdotto().getQuantitaDisponibile()));
                            rowSubtotal = r.getSubtotale();
                            success = true;
                            message = "Quantità aggiornata.";
                            break;
                        }
                    }
                }
            } else if ("remove".equalsIgnoreCase(action) && idParam != null) {
                int id = Integer.parseInt(idParam.trim());
                carrello.rimuoviProdotto(id);
                success = true;
                message = "Articolo rimosso dal carrello.";
            } else if ("clear".equalsIgnoreCase(action)) {
                carrello.svuota();
                success = true;
                message = "Carrello svuotato.";
            }

            if (success && loggedUser != null) {
                carrelloDAO.doSaveOrUpdate(loggedUser.getId(), carrello);
            }

            String jsonResponse = String.format(Locale.US,
                "{\"success\":%b,\"message\":\"%s\",\"totalCount\":%d,\"cartTotal\":%.2f,\"rowSubtotal\":%.2f}",
                success, escapeJson(message), carrello.getNumeroProdotti(), carrello.getTotale(), rowSubtotal
            );

            out.print(jsonResponse);
            out.flush();

        } catch (NumberFormatException | SQLException | NamingException e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Errore durante l'elaborazione del carrello.\"}");
            out.flush();
        }
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\"", "\\\"");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
