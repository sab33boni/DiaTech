package it.unisa.diatech.control.admin;

import it.unisa.diatech.dao.BrandDAO;
import it.unisa.diatech.dao.CategoriaDAO;
import it.unisa.diatech.dao.ProdottoDAO;
import it.unisa.diatech.model.Brand;
import it.unisa.diatech.model.Categoria;
import it.unisa.diatech.model.Prodotto;
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

@WebServlet(name = "AdminProdottiServlet", urlPatterns = {"/admin/prodotti"})
public class AdminProdottiServlet extends HttpServlet {

    private ProdottoDAO prodottoDAO;
    private CategoriaDAO categoriaDAO;
    private BrandDAO brandDAO;

    @Override
    public void init() throws ServletException {
        this.prodottoDAO = new ProdottoDAO();
        this.categoriaDAO = new CategoriaDAO();
        this.brandDAO = new BrandDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SessionUtil.isAdmin(request)) {
            response.sendRedirect(request.getContextPath() + "/login?error=unauthorized_admin");
            return;
        }

        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        try {
            if ("delete".equalsIgnoreCase(action) && idParam != null) {
                int id = Integer.parseInt(idParam.trim());
                prodottoDAO.doDelete(id);
                response.sendRedirect(request.getContextPath() + "/admin/prodotti?success=deleted");
                return;
            }

            List<Prodotto> prodotti = prodottoDAO.doRetrieveAllAdmin();
            List<Categoria> categorie = categoriaDAO.doRetrieveAll();
            List<Brand> brands = brandDAO.doRetrieveAll();

            request.setAttribute("prodotti", prodotti);
            request.setAttribute("categorie", categorie);
            request.setAttribute("brands", brands);

            if ("deleted".equals(request.getParameter("success"))) {
                request.setAttribute("successMessage", "Dispositivo cancellato logicamente dal catalogo (Soft-Delete effettuato).");
            } else if ("created".equals(request.getParameter("success"))) {
                request.setAttribute("successMessage", "Nuovo dispositivo aggiunto con successo al catalogo.");
            } else if ("updated".equals(request.getParameter("success"))) {
                request.setAttribute("successMessage", "Dati del dispositivo aggiornati con successo.");
            }

            request.getRequestDispatcher("/WEB-INF/view/admin/gestione-prodotti.jsp").forward(request, response);

        } catch (SQLException | NamingException | NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nella gestione prodotti admin.");
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
        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String descrizione = request.getParameter("descrizione");
        String prezzoParam = request.getParameter("prezzo");
        String quantitaParam = request.getParameter("quantita");
        String immagine = request.getParameter("immagine");
        String idCatParam = request.getParameter("idCategoria");
        String idBrandParam = request.getParameter("idBrand");

        try {
            double prezzo = Double.parseDouble(prezzoParam.trim());
            int quantita = Integer.parseInt(quantitaParam.trim());
            int idCategoria = Integer.parseInt(idCatParam.trim());
            int idBrand = Integer.parseInt(idBrandParam.trim());

            Categoria categoria = categoriaDAO.doRetrieveByKey(idCategoria);
            Brand brand = brandDAO.doRetrieveByKey(idBrand);

            Prodotto p = new Prodotto();
            p.setNome(nome.trim());
            p.setDescrizione(descrizione != null ? descrizione.trim() : "");
            p.setPrezzo(prezzo);
            p.setQuantitaDisponibile(quantita);
            p.setImmagine(immagine != null && !immagine.trim().isEmpty() ? immagine.trim() : "default.png");
            p.setCategoria(categoria);
            p.setBrand(brand);
            p.setCancellato(false);

            if ("update".equalsIgnoreCase(action) && idParam != null && !idParam.trim().isEmpty()) {
                p.setId(Integer.parseInt(idParam.trim()));
                prodottoDAO.doUpdate(p);
                response.sendRedirect(request.getContextPath() + "/admin/prodotti?success=updated");
            } else {
                prodottoDAO.doSave(p);
                response.sendRedirect(request.getContextPath() + "/admin/prodotti?success=created");
            }

        } catch (SQLException | NamingException | NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/prodotti?error=invalid_data");
        }
    }
}
