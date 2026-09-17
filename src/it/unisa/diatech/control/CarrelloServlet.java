package it.unisa.diatech.control;

import it.unisa.diatech.dao.CarrelloDAO;
import it.unisa.diatech.dao.ProdottoDAO;
import it.unisa.diatech.model.Carrello;
import it.unisa.diatech.model.Prodotto;
import it.unisa.diatech.model.Utente;
import it.unisa.diatech.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import javax.naming.NamingException;

@WebServlet(name = "CarrelloServlet", urlPatterns = {"/carrello"})
public class CarrelloServlet extends HttpServlet {

    private ProdottoDAO prodottoDAO;
    private CarrelloDAO carrelloDAO;

    @Override
    public void init() throws ServletException {
        this.prodottoDAO = new ProdottoDAO();
        this.carrelloDAO = new CarrelloDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SessionUtil.getCart(request);
        request.getRequestDispatcher("/WEB-INF/view/carrello.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String idParam = request.getParameter("idProdotto");
        String qtyParam = request.getParameter("quantita");

        Carrello carrello = SessionUtil.getCart(request);
        Utente loggedUser = SessionUtil.getLoggedUser(request);

        try {
            if ("add".equalsIgnoreCase(action) && idParam != null) {
                int id = Integer.parseInt(idParam.trim());
                int quantita = qtyParam != null ? Math.max(1, Integer.parseInt(qtyParam.trim())) : 1;

                Prodotto p = prodottoDAO.doRetrieveByKey(id);
                if (p != null && !p.isCancellato()) {
                    carrello.aggiungiProdotto(p, quantita);
                }
            } else if ("update".equalsIgnoreCase(action) && idParam != null && qtyParam != null) {
                int id = Integer.parseInt(idParam.trim());
                int quantita = Integer.parseInt(qtyParam.trim());

                if (quantita <= 0) {
                    carrello.rimuoviProdotto(id);
                } else {
                    carrello.getRighe().forEach(r -> {
                        if (r.getProdotto().getId() == id) {
                            r.setQuantita(Math.min(quantita, r.getProdotto().getQuantitaDisponibile()));
                        }
                    });
                }
            } else if ("remove".equalsIgnoreCase(action) && idParam != null) {
                int id = Integer.parseInt(idParam.trim());
                carrello.rimuoviProdotto(id);
            } else if ("clear".equalsIgnoreCase(action)) {
                carrello.svuota();
            }

            if (loggedUser != null) {
                carrelloDAO.doSaveOrUpdate(loggedUser.getId(), carrello);
            }

            response.sendRedirect(request.getContextPath() + "/carrello");

        } catch (NumberFormatException | SQLException | NamingException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/carrello");
        }
    }
}
