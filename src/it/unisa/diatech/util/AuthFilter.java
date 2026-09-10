package it.unisa.diatech.util;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtro di sicurezza che intercetta le richieste HTTP per verificare i permessi di accesso.
 * - Le rotte /admin/* richiedono obbligatoriamente il ruolo ADMIN.
 * - Le rotte /checkout e /ordini richiedono che l'utente sia autenticato (REGISTRATO o ADMIN).
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/admin/*", "/checkout", "/ordini"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inizializzazione del filtro (se necessaria)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getServletPath();
        String contextPath = httpRequest.getContextPath();

        // 1. Controllo per l'area Riservata Amministratore (/admin/*)
        if (path.startsWith("/admin")) {
            if (!SessionUtil.isAdmin(httpRequest)) {
                // Non è admin: reindirizza alla pagina di login con messaggio di errore
                httpResponse.sendRedirect(contextPath + "/login?error=unauthorized_admin");
                return;
            }
        }

        // 2. Controllo per le aree riservate ai clienti autenticati (/checkout, /ordini)
        if (path.equals("/checkout") || path.equals("/ordini")) {
            if (!SessionUtil.isUserLoggedIn(httpRequest)) {
                // Utente non autenticato: reindirizza al login memorizzando l'URL di provenienza
                httpResponse.sendRedirect(contextPath + "/login?redirect=" + path);
                return;
            }
        }

        // Se tutti i controlli sono superati, prosegui lungo la catena di filtri / servlet
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Rilascio delle risorse del filtro
    }
}
