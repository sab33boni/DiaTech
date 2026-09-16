package it.unisa.diatech.control;

import it.unisa.diatech.dao.BrandDAO;
import it.unisa.diatech.dao.CategoriaDAO;
import it.unisa.diatech.dao.ProdottoDAO;
import it.unisa.diatech.model.Brand;
import it.unisa.diatech.model.Categoria;
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

@WebServlet(name = "CatalogoServlet", urlPatterns = {"/catalogo"})
public class CatalogoServlet extends HttpServlet {

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
        try {
            List<Categoria> categorie = categoriaDAO.doRetrieveAll();
            List<Brand> brands = brandDAO.doRetrieveAll();
            request.setAttribute("categorie", categorie);
            request.setAttribute("brands", brands);

            String keyword = request.getParameter("q");
            String catParam = request.getParameter("categoria");
            String brandParam = request.getParameter("brand");
            String minPriceParam = request.getParameter("minPrezzo");
            String maxPriceParam = request.getParameter("maxPrezzo");
            String orderBy = request.getParameter("order");

            Integer idCategoria = parseInteger(catParam);
            Integer idBrand = parseInteger(brandParam);
            Double minPrezzo = parseDouble(minPriceParam);
            Double maxPrezzo = parseDouble(maxPriceParam);

            List<Prodotto> prodotti;

            if (keyword != null && !keyword.trim().isEmpty()) {
                prodotti = prodottoDAO.doRetrieveBySearch(keyword);
            } else {
                prodotti = prodottoDAO.doRetrieveByFiltri(idCategoria, idBrand, minPrezzo, maxPrezzo, orderBy);
            }

            request.setAttribute("prodotti", prodotti);
            request.getRequestDispatcher("/WEB-INF/view/catalogo.jsp").forward(request, response);

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore catalogo");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private Integer parseInteger(String val) {
        if (val != null && !val.trim().isEmpty()) {
            try {
                return Integer.parseInt(val.trim());
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    private Double parseDouble(String val) {
        if (val != null && !val.trim().isEmpty()) {
            try {
                return Double.parseDouble(val.trim());
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }
}
