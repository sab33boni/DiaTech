package it.unisa.diatech.util;

import it.unisa.diatech.model.Carrello;
import it.unisa.diatech.model.Utente;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Classe di utilità per la gestione della sessione HTTP, dello stato di autenticazione,
 * del carrello in sessione e del token di sicurezza per la protezione delle richieste.
 */
public class SessionUtil {

    public static final String ATTRIBUTE_USER = "loggedUser";
    public static final String ATTRIBUTE_CART = "carrello";
    public static final String ATTRIBUTE_TOKEN = "sessionToken";

    private SessionUtil() {}

    /**
     * Recupera l'utente attualmente autenticato dalla sessione.
     *
     * @param request Oggetto HttpServletRequest
     * @return Oggetto Utente se loggato, null altrimenti
     */
    public static Utente getLoggedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Utente) session.getAttribute(ATTRIBUTE_USER);
        }
        return null;
    }

    /**
     * Verifica se esiste un utente autenticato nella sessione corrente.
     */
    public static boolean isUserLoggedIn(HttpServletRequest request) {
        return getLoggedUser(request) != null;
    }

    /**
     * Verifica se l'utente attualmente autenticato possiede i permessi di Amministratore (ADMIN).
     */
    public static boolean isAdmin(HttpServletRequest request) {
        Utente user = getLoggedUser(request);
        return user != null && user.isAdmin();
    }

    /**
     * Recupera il carrello dalla sessione. Se non è presente, ne crea uno nuovo e lo associa alla sessione.
     */
    public static Carrello getCart(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Carrello cart = (Carrello) session.getAttribute(ATTRIBUTE_CART);
        if (cart == null) {
            cart = new Carrello();
            session.setAttribute(ATTRIBUTE_CART, cart);
        }
        return cart;
    }

    /**
     * Esegue il login dell'utente: registra l'oggetto Utente in sessione
     * e genera un nuovo Token di sessione univoco (UUID) per il controllo di sicurezza.
     */
    public static void login(HttpServletRequest request, Utente user) {
        HttpSession session = request.getSession(true);
        session.setAttribute(ATTRIBUTE_USER, user);
        
        // Generazione del token di sessione univoco
        String token = UUID.randomUUID().toString();
        session.setAttribute(ATTRIBUTE_TOKEN, token);
    }

    /**
     * Esegue il logout dell'utente invalidando la sessione corrente.
     */
    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    /**
     * Valida il token di sessione inviato con una richiesta (protezione CSRF/access control).
     */
    public static boolean isValidToken(HttpServletRequest request, String token) {
        HttpSession session = request.getSession(false);
        if (session == null || token == null) {
            return false;
        }
        String sessionToken = (String) session.getAttribute(ATTRIBUTE_TOKEN);
        return token.equals(sessionToken);
    }
}
