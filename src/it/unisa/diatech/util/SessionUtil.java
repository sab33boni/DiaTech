package it.unisa.diatech.util;

import it.unisa.diatech.model.Carrello;
import it.unisa.diatech.model.Utente;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.UUID;

public class SessionUtil {

    public static final String ATTRIBUTE_USER = "loggedUser";
    public static final String ATTRIBUTE_CART = "carrello";
    public static final String ATTRIBUTE_TOKEN = "sessionToken";

    private SessionUtil() {}

    
    public static Utente getLoggedUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Utente) session.getAttribute(ATTRIBUTE_USER);
        }
        return null;
    }

    
    public static boolean isUserLoggedIn(HttpServletRequest request) {
        return getLoggedUser(request) != null;
    }

    
    public static boolean isAdmin(HttpServletRequest request) {
        Utente user = getLoggedUser(request);
        return user != null && user.isAdmin();
    }

    
    public static Carrello getCart(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Carrello cart = (Carrello) session.getAttribute(ATTRIBUTE_CART);
        if (cart == null) {
            cart = new Carrello();
            session.setAttribute(ATTRIBUTE_CART, cart);
        }
        return cart;
    }

    
    public static void login(HttpServletRequest request, Utente user) {
        HttpSession session = request.getSession(true);
        session.setAttribute(ATTRIBUTE_USER, user);
        String token = UUID.randomUUID().toString();
        session.setAttribute(ATTRIBUTE_TOKEN, token);
    }

    
    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    
    public static boolean isValidToken(HttpServletRequest request, String token) {
        HttpSession session = request.getSession(false);
        if (session == null || token == null) {
            return false;
        }
        String sessionToken = (String) session.getAttribute(ATTRIBUTE_TOKEN);
        return token.equals(sessionToken);
    }
}
