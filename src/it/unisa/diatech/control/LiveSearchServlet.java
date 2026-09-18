package it.unisa.diatech.control;

import it.unisa.diatech.dao.ProdottoDAO;
import it.unisa.diatech.model.Prodotto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;

@WebServlet(name = "LiveSearchServlet", urlPatterns = {"/live-search"})
public class LiveSearchServlet extends HttpServlet {

    private ProdottoDAO prodottoDAO;

    @Override
    public void init() throws ServletException {
        this.prodottoDAO = new ProdottoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String query = request.getParameter("q");

        if (query == null || query.trim().length() < 2) {
            out.print("[]");
            out.flush();
            return;
        }

        try {
            List<Prodotto> risultati = prodottoDAO.doRetrieveBySearch(query.trim());
            List<Prodotto> limitati = risultati.size() > 5 ? risultati.subList(0, 5) : risultati;

            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < limitati.size(); i++) {
                Prodotto p = limitati.get(i);
                json.append("{")
                    .append("\"id\":").append(p.getId()).append(",")
                    .append("\"nome\":\"").append(escapeJson(p.getNome())).append("\",")
                    .append("\"prezzo\":").append(p.getPrezzo()).append(",")
                    .append("\"immagine\":\"").append(escapeJson(p.getImmagine())).append("\",")
                    .append("\"categoria\":\"").append(escapeJson(p.getCategoria().getNome())).append("\",")
                    .append("\"brand\":\"").append(escapeJson(p.getBrand().getNome())).append("\"")
                    .append("}");

                if (i < limitati.size() - 1) {
                    json.append(",");
                }
            }
            json.append("]");

            out.print(json.toString());
            out.flush();

        } catch (SQLException | NamingException e) {
            e.printStackTrace();
            out.print("[]");
            out.flush();
        }
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\b", "\\b")
                  .replace("\f", "\\f")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
