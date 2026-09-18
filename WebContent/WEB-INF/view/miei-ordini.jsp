<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="DiaTech Solutions | I Miei Ordini" scope="request" />
<jsp:include page="/WEB-INF/view/common/header.jsp" />
<jsp:include page="/WEB-INF/view/common/navbar.jsp" />

<main class="main-content orders-history-page">
    <div class="orders-container">

        <div class="orders-header">
            <h1 class="orders-title">I Miei Ordini & Storico Acquisti</h1>
            <p class="orders-subtitle">
                Consulta la cronologia dei tuoi ordini, lo stato di spedizione e i certificati di garanzia legale associati.
            </p>
        </div>

        <c:choose>
            <c:when test="${not empty ordini}">
                <div class="orders-list">
                    <c:forEach var="ordine" items="${ordini}">
                        <article class="order-card" id="order-${ordine.id}">

                            <!-- HEADER DELLA CARD ORDINE -->
                            <div class="order-card-header">
                                <div class="order-header-left">
                                    <span class="order-id">Ordine <strong>#ORD-${ordine.id}</strong></span>
                                    <span class="order-date">📅 ${ordine.dataOrdine}</span>
                                </div>
                                <div class="order-header-right">
                                    <span class="status-badge status-${ordine.stato}">
                                        ${ordine.stato}
                                    </span>
                                </div>
                            </div>

                            <!-- TABELLA ARTICOLI CON PREZZI CONGELATI -->
                            <div class="order-card-body">
                                <table class="order-history-table">
                                    <thead>
                                        <tr>
                                            <th>Dispositivo Medico</th>
                                            <th>Prezzo all'Acquisto</th>
                                            <th>Qtà</th>
                                            <th>Subtotale</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="riga" items="${ordine.righe}">
                                            <tr>
                                                <td class="td-history-prod">
                                                    <div class="history-prod-box">
                                                        <a href="${pageContext.request.contextPath}/prodotto?id=${riga.prodotto.id}">
                                                            <img src="${pageContext.request.contextPath}/images/prodotti/${riga.prodotto.immagine}"
                                                                 alt="${riga.prodotto.nome}"
                                                                 class="history-prod-img"
                                                                 onerror="this.src='${pageContext.request.contextPath}/images/prodotti/default.png';">
                                                        </a>
                                                        <div>
                                                            <span class="prod-brand-tag">${riga.prodotto.brand.nome}</span>
                                                            <a href="${pageContext.request.contextPath}/prodotto?id=${riga.prodotto.id}" class="prod-name-link">
                                                                ${riga.prodotto.nome}
                                                            </a>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td>
                                                    <!-- Prezzo congelato al momento dell'acquisto -->
                                                    <fmt:formatNumber value="${riga.prezzoUnitario}" type="currency" currencySymbol="€" />
                                                </td>
                                                <td>${riga.quantita}</td>
                                                <td>
                                                    <strong>
                                                        <fmt:formatNumber value="${riga.subtotale}" type="currency" currencySymbol="€" />
                                                    </strong>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                            <!-- FOOTER DELLA CARD: SPEDIZIONE, GARANZIA E TOTALE -->
                            <div class="order-card-footer">
                                <div class="footer-info-group">
                                    <div class="footer-info-item">
                                        <span class="info-label">📍 Consegna a:</span>
                                        <span>${ordine.indirizzoSpedizione}, ${ordine.cap} ${ordine.citta}</span>
                                    </div>
                                    <div class="footer-info-item">
                                        <span class="info-label">💳 Pagamento:</span>
                                        <span>${ordine.metodoPagamento}</span>
                                    </div>
                                    <div class="footer-info-item warranty-active-badge">
                                        <span>🛡️ Garanzia Legale 2 Anni Attiva</span>
                                    </div>
                                </div>

                                <div class="footer-total-group">
                                    <span class="total-label">Totale Ordine:</span>
                                    <strong class="total-price">
                                        <fmt:formatNumber value="${ordine.totale}" type="currency" currencySymbol="€" />
                                    </strong>
                                </div>
                            </div>

                        </article>
                    </c:forEach>
                </div>
            </c:when>
<c:otherwise>
                <div class="no-orders-box">
                    <div class="no-orders-icon">📦</div>
                    <h2>Nessun ordine effettuato finora</h2>
                    <p>Non hai ancora effettuato acquisti di dispositivi medici o accessori.</p>
                    <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-primary btn-lg">
                        Esplora il Catalogo Prodotti ➔
                    </a>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</main>

<jsp:include page="/WEB-INF/view/common/footer.jsp" />
