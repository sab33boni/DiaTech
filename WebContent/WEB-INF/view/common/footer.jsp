<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<footer class="site-footer">
    <div class="footer-container">
        <!-- Colonna 1: Info Azienda & Disclaimer Medico -->
        <div class="footer-col">
            <h3 class="footer-title">Dia<strong>Tech</strong> Solutions</h3>
            <p class="footer-text">
                Piattaforma e-commerce B2C specializzata nella fornitura di dispositivi medici certificati per il monitoraggio avanzato del diabete e la cura del paziente.
            </p>
            <p class="footer-disclaimer">
                <small>⚠️ I dispositivi medici in vendita sono conformi alle normative CE. Consultare sempre il proprio medico specialista diabetologo prima dell'uso.</small>
            </p>
        </div>

        <!-- Colonna 2: Link Rapidi al Catalogo -->
        <div class="footer-col">
            <h4 class="footer-subtitle">Categorie</h4>
            <ul class="footer-links">
                <li><a href="${pageContext.request.contextPath}/catalogo?categoria=1">Sensori CGM</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo?categoria=2">Glucometri Digitali</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo?categoria=3">Strisce Reattive</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo?categoria=4">Pungidito e Lancette</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo?categoria=5">Accessori e Protezioni</a></li>
            </ul>
        </div>

        <!-- Colonna 3: Servizio Clienti & Garanzie -->
        <div class="footer-col">
            <h4 class="footer-subtitle">Assistenza & Garanzie</h4>
            <ul class="footer-links">
                <li><a href="${pageContext.request.contextPath}/home#servizi">Garanzia Legale 2 Anni</a></li>
                <li><a href="${pageContext.request.contextPath}/home#spedizioni">Spedizioni Termocontrollate</a></li>
                <li><a href="${pageContext.request.contextPath}/carrello">Carrello e Ordini</a></li>
                <li><a href="${pageContext.request.contextPath}/ordini">I Miei Ordini</a></li>
            </ul>
        </div>

        <!-- Colonna 4: Contatti & Info Universitarie -->
        <div class="footer-col">
            <h4 class="footer-subtitle">Contatti & Progetto</h4>
            <p class="footer-contact">📍 Università degli Studi di Salerno</p>
            <p class="footer-contact">📚 Progetto Tecnologie e Sistemi Web (TSW)</p>
            <p class="footer-contact">✉️ info@diatech.it</p>
            <p class="footer-contact">📞 Numero Verde: 800-123-456</p>
        </div>
    </div>

    <div class="footer-bottom">
        <p>&copy; 2026 DiaTech Solutions. Tutti i diritti riservati. Progetto didattico per il corso TSW - UNISA.</p>
    </div>
</footer>

<!-- Inizializzazione della variabile globale contextPath per gli script JS -->
<script>
    window.contextPath = '${pageContext.request.contextPath}';
</script>

<!-- Script JavaScript Esterni Globali (conformi alla separazione frontend) -->
<script src="${pageContext.request.contextPath}/scripts/live-search.js"></script>
<script src="${pageContext.request.contextPath}/scripts/carrello-ajax.js"></script>

<!-- Script aggiuntivo specifico della pagina (es. validazione.js) -->
<c:if test="${not empty extraJs}">
    <script src="${pageContext.request.contextPath}/scripts/${extraJs}"></script>
</c:if>

</body>
</html>
