<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="DiaTech Solutions | Ordine Confermato" scope="request" />
<jsp:include page="/WEB-INF/view/common/header.jsp" />
<jsp:include page="/WEB-INF/view/common/navbar.jsp" />

<main class="main-content order-confirmation-page">
    <div class="confirmation-container">

        <c:choose>
            <c:when test="${not empty ordine}">
                <!-- BANNER DI SUCCESSO -->
                <div class="success-banner">
                    <div class="success-icon-circle">✓</div>
                    <h1 class="success-title">Grazie per il tuo Ordine, ${sessionScope.loggedUser.nome}!</h1>
                    <p class="success-subtitle">
                        Il tuo ordine <strong>#ORD-${ordine.id}</strong> è stato registrato con successo ed è ora <em>${ordine.stato}</em>.
                    </p>
                    <p class="success-notice">
                        Abbiamo inviato un'email di riepilogo a <strong>${sessionScope.loggedUser.email}</strong>.
                    </p>
                </div>

                <div class="confirmation-layout">

                    <!-- COLONNA SINISTRA: DETTAGLIO ARTICOLI ACQUISTATI -->
                    <section class="order-items-card">
                        <div class="card-header">
                            <h3>Dispositivi Medici Acquistati</h3>
                        </div>

                        <div class="order-items-table-wrapper">
                            <table class="order-items-table">
                                <thead>
                                    <tr>
                                        <th>Dispositivo</th>
                                        <th>Prezzo Unitario</th>
                                        <th>Quantità</th>
                                        <th>Totale</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="riga" items="${ordine.righe}">
                                        <tr>
                                            <td class="td-order-prod">
                                                <div class="order-prod-item">
                                                    <img src="${pageContext.request.contextPath}/images/prodotti/${riga.prodotto.immagine}"
                                                         alt="${riga.prodotto.nome}"
                                                         class="order-prod-thumb"
                                                         onerror="this.src='${pageContext.request.contextPath}/images/prodotti/default.png';">
                                                    <div>
                                                        <span class="order-prod-brand">${riga.prodotto.brand.nome}</span>
                                                        <strong>${riga.prodotto.nome}</strong>
                                                    </div>
                                                </div>
                                            </td>
                                            <td>
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

                        <!-- Box Garanzia Legale Associata -->
                        <div class="warranty-active-box">
                            <span class="w-icon">🛡️</span>
                            <div class="w-info">
                                <strong>Garanzia Legale di 2 Anni Attivata Automaticamente</strong>
                                <p>Tutti i dispositivi elettromedicali presenti in questo ordine sono coperti da garanzia legale per 24 mesi dalla data odierna.</p>
                            </div>
                        </div>
                    </section>

                    <!-- COLONNA DESTRA: RIEPILOGO SPEDIZIONE E PAGAMENTO -->
                    <aside class="order-details-card">
                        <div class="card-header">
                            <h3>Dettagli Spedizione & Pagamento</h3>
                        </div>

                        <div class="details-body">
                            <div class="detail-block">
                                <span class="detail-label">Destinatario:</span>
                                <strong>${sessionScope.loggedUser.nomeCompleto}</strong>
                            </div>

                            <div class="detail-block">
                                <span class="detail-label">Indirizzo di Consegna:</span>
                                <span>${ordine.indirizzoSpedizione}, ${ordine.cap} ${ordine.citta}</span>
                            </div>

                            <div class="detail-block">
                                <span class="detail-label">Metodo di Pagamento:</span>
                                <span>💳 ${ordine.metodoPagamento}</span>
                            </div>

                            <div class="detail-block">
                                <span class="detail-label">Data e Ora Ordine:</span>
                                <span>${ordine.dataOrdine}</span>
                            </div>

                            <div class="detail-divider"></div>

                            <div class="detail-total-box">
                                <span>Totale Pagato:</span>
                                <strong class="order-final-total">
                                    <fmt:formatNumber value="${ordine.totale}" type="currency" currencySymbol="€" />
                                </strong>
                            </div>

                            <div class="confirmation-cta-group">
                                <a href="${pageContext.request.contextPath}/ordini" class="btn btn-primary btn-block">
                                    📦 Vai ai Miei Ordini
                                </a>
                                <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-outline btn-block">
                                    Continua gli Acquisti
                                </a>
                            </div>
                        </div>
                    </aside>

                </div>
            </c:when>
            <c:otherwise>
                <div class="no-order-box">
                    <h2>Nessun ordine recente trovato</h2>
                    <p>Puoi consultare i tuoi ordini passati nella sezione dedicata del tuo profilo.</p>
                    <a href="${pageContext.request.contextPath}/ordini" class="btn btn-primary">
                        I Miei Ordini ➔
                    </a>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</main>

<jsp:include page="/WEB-INF/view/common/footer.jsp" />
