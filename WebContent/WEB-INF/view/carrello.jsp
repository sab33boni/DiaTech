<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="DiaTech Solutions | Il Tuo Carrello" scope="request" />
<jsp:include page="/WEB-INF/view/common/header.jsp" />
<jsp:include page="/WEB-INF/view/common/navbar.jsp" />

<main class="main-content cart-page">
    <div class="cart-container">

        <div class="cart-header">
            <h1 class="cart-title">Il Tuo Carrello Spesa</h1>
            <p class="cart-subtitle">Dispositivi medici e consumabili pronti per l'ordine</p>
        </div>

        <c:choose>
            <c:when test="${not empty sessionScope.carrello && not empty sessionScope.carrello.righe}">
                <div class="cart-layout">

                    <!-- COLONNA SINISTRA: TABELLA DEGLI ARTICOLI -->
                    <section class="cart-items-section">
                        <div class="cart-items-table-wrapper">
                            <table class="cart-table">
                                <thead>
                                    <tr>
                                        <th class="th-prod">Prodotto</th>
                                        <th class="th-price">Prezzo Unitario</th>
                                        <th class="th-qty">Quantità</th>
                                        <th class="th-subtotal">Subtotale</th>
                                        <th class="th-action">Rimuovi</th>
                                    </tr>
                                </thead>
                                <tbody id="cart-table-body">
                                    <c:forEach var="riga" items="${sessionScope.carrello.righe}">
                                        <tr class="cart-row" id="cart-row-${riga.prodotto.id}">
                                            <!-- Info Prodotto -->
                                            <td class="td-product">
                                                <div class="cart-prod-card">
                                                    <a href="${pageContext.request.contextPath}/prodotto?id=${riga.prodotto.id}" class="cart-prod-thumb-link">
                                                        <img src="${pageContext.request.contextPath}/images/prodotti/${riga.prodotto.immagine}"
                                                             alt="${riga.prodotto.nome}"
                                                             class="cart-prod-thumb"
                                                             onerror="this.src='${pageContext.request.contextPath}/images/prodotti/default.png';">
                                                    </a>
                                                    <div class="cart-prod-details">
                                                        <span class="cart-prod-brand">${riga.prodotto.brand.nome}</span>
                                                        <h4 class="cart-prod-name">
                                                            <a href="${pageContext.request.contextPath}/prodotto?id=${riga.prodotto.id}">
                                                                ${riga.prodotto.nome}
                                                            </a>
                                                        </h4>
                                                        <span class="cart-prod-category">${riga.prodotto.categoria.nome}</span>
                                                    </div>
                                                </div>
                                            </td>

                                            <!-- Prezzo Unitario -->
                                            <td class="td-price" data-label="Prezzo">
                                                <span class="unit-price">
                                                    <fmt:formatNumber value="${riga.prodotto.prezzo}" type="currency" currencySymbol="€" />
                                                </span>
                                            </td>

                                            <!-- Selettore Quantità Dinamico AJAX -->
                                            <td class="td-qty" data-label="Quantità">
                                                <div class="cart-qty-control">
                                                    <button type="button"
                                                            class="btn-cart-qty btn-cart-minus"
                                                            data-id="${riga.prodotto.id}">-</button>
                                                    <input type="number"
                                                           class="cart-qty-input"
                                                           value="${riga.quantita}"
                                                           min="1"
                                                           max="${riga.prodotto.quantitaDisponibile}"
                                                           data-id="${riga.prodotto.id}"
                                                           readonly>
                                                    <button type="button"
                                                            class="btn-cart-qty btn-cart-plus"
                                                            data-id="${riga.prodotto.id}">+</button>
                                                </div>
                                            </td>

                                            <!-- Subtotale Riga Calcolato -->
                                            <td class="td-subtotal" data-label="Subtotale">
                                                <strong class="row-subtotal" id="subtotal-${riga.prodotto.id}">
                                                    <fmt:formatNumber value="${riga.subtotale}" type="currency" currencySymbol="€" />
                                                </strong>
                                            </td>

                                            <!-- Pulsante Rimuovi Articolo AJAX -->
                                            <td class="td-action" data-label="Azione">
                                                <button type="button"
                                                        class="btn-remove-item btn-remove-cart-ajax"
                                                        data-id="${riga.prodotto.id}"
                                                        data-nome="${riga.prodotto.nome}"
                                                        title="Rimuovi dal carrello">
                                                    🗑️
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <div class="cart-actions-row">
                            <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-outline">
                                ➔ Continua gli Acquisti
                            </a>
                            <button type="button" class="btn btn-danger-outline" id="btn-clear-cart">
                                🗑️ Svuota Carrello
                            </button>
                        </div>
                    </section>

                    <!-- COLONNA DESTRA: RIEPILOGO COSTI E CHECKOUT -->
                    <aside class="cart-summary-section">
                        <div class="summary-card">
                            <h3 class="summary-title">Riepilogo Ordine</h3>

                            <div class="summary-line">
                                <span>Articoli nel carrello:</span>
                                <strong id="summary-items-count">${sessionScope.carrello.numeroProdotti}</strong>
                            </div>

                            <div class="summary-line">
                                <span>Subtotale Dispositivi:</span>
                                <span id="summary-subtotal">
                                    <fmt:formatNumber value="${sessionScope.carrello.totale}" type="currency" currencySymbol="€" />
                                </span>
                            </div>

                            <div class="summary-line">
                                <span>Spedizione Termocontrollata:</span>
                                <span class="shipping-free">GRATIS</span>
                            </div>

                            <div class="summary-divider"></div>

                            <div class="summary-total-line">
                                <span>Totale Complessivo:</span>
                                <strong class="total-amount" id="summary-total">
                                    <fmt:formatNumber value="${sessionScope.carrello.totale}" type="currency" currencySymbol="€" />
                                </strong>
                            </div>

                            <div class="summary-vat-notice">
                                <small>IVA inclusa e detraibile come spesa sanitaria (dispositivi CE)</small>
                            </div>

                            <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary btn-block btn-checkout">
                                Procedi al Checkout ➔
                            </a>

                            <div class="trust-icons-list">
                                <div class="trust-mini-item">🔒 Pagamento Sicuro e Protetto</div>
                                <div class="trust-mini-item">🛡️ Garanzia Legale 2 Anni Inclusa</div>
                                <div class="trust-mini-item">📦 Consegna Rapida in 24/48h</div>
                            </div>
                        </div>
                    </aside>

                </div>
            </c:when>
<c:otherwise>
                <div class="empty-cart-box">
                    <div class="empty-cart-icon">🛒</div>
                    <h2>Il tuo carrello è attualmente vuoto</h2>
                    <p>Non hai ancora aggiunto nessun dispositivo o consumabile per la cura del diabete.</p>
                    <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-primary btn-lg">
                        Esplora il Catalogo Prodotti ➔
                    </a>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</main>

<jsp:include page="/WEB-INF/view/common/footer.jsp" />
