<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="DiaTech Solutions | Cassa e Checkout" scope="request" />
<c:set var="extraJs" value="validazione.js" scope="request" />
<jsp:include page="/WEB-INF/view/common/header.jsp" />
<jsp:include page="/WEB-INF/view/common/navbar.jsp" />

<main class="main-content checkout-page">
    <div class="checkout-container">

        <div class="checkout-header">
            <h1 class="checkout-title">Completamento dell'Ordine</h1>
            <p class="checkout-subtitle">Inserisci l'indirizzo di consegna sanitaria e seleziona il metodo di pagamento</p>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger" role="alert">
                ⚠️ ${errorMessage}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/checkout" method="POST" class="checkout-form" id="form-checkout" novalidate>
            <!-- Token di sicurezza anti-CSRF -->
            <input type="hidden" name="sessionToken" value="${sessionScope.sessionToken}">

            <div class="checkout-layout">

                <!-- COLONNA SINISTRA: DATI SPEDIZIONE E PAGAMENTO -->
                <section class="checkout-form-section">

                    <!-- SEZIONE 1: INDIRIZZO DI SPEDIZIONE -->
                    <div class="checkout-card">
                        <div class="checkout-card-header">
                            <span class="step-num">1</span>
                            <h3>Indirizzo di Spedizione Sanitaria</h3>
                        </div>

                        <div class="form-row-2">
                            <div class="form-group">
                                <label for="check-nome" class="form-label">Nome Destinatario *</label>
                                <input type="text"
                                       id="check-nome"
                                       name="nome"
                                       class="form-control"
                                       value="${sessionScope.loggedUser.nome}"
                                       required>
                                <span class="field-error" id="error-check-nome"></span>
                            </div>
                            <div class="form-group">
                                <label for="check-cognome" class="form-label">Cognome Destinatario *</label>
                                <input type="text"
                                       id="check-cognome"
                                       name="cognome"
                                       class="form-control"
                                       value="${sessionScope.loggedUser.cognome}"
                                       required>
                                <span class="field-error" id="error-check-cognome"></span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="check-indirizzo" class="form-label">Indirizzo di Consegna (Via, Piazza, Civico) *</label>
                            <input type="text"
                                   id="check-indirizzo"
                                   name="indirizzo"
                                   class="form-control"
                                   placeholder="Es. Via Roma 15"
                                   value="${sessionScope.loggedUser.indirizzo}"
                                   required>
                            <span class="field-error" id="error-check-indirizzo"></span>
                        </div>

                        <div class="form-row-3">
                            <div class="form-group flex-2">
                                <label for="check-citta" class="form-label">Città *</label>
                                <input type="text"
                                       id="check-citta"
                                       name="citta"
                                       class="form-control"
                                       placeholder="Es. Salerno"
                                       value="${sessionScope.loggedUser.citta}"
                                       required>
                                <span class="field-error" id="error-check-citta"></span>
                            </div>
                            <div class="form-group flex-1">
                                <label for="check-cap" class="form-label">CAP *</label>
                                <input type="text"
                                       id="check-cap"
                                       name="cap"
                                       class="form-control"
                                       placeholder="84100"
                                       maxlength="5"
                                       value="${sessionScope.loggedUser.cap}"
                                       required>
                                <span class="field-error" id="error-check-cap"></span>
                            </div>
                            <div class="form-group flex-2">
                                <label for="check-telefono" class="form-label">Telefono per il Corriere *</label>
                                <input type="tel"
                                       id="check-telefono"
                                       name="telefono"
                                       class="form-control"
                                       placeholder="Es. 3331234567"
                                       value="${sessionScope.loggedUser.telefono}"
                                       required>
                                <span class="field-error" id="error-check-telefono"></span>
                            </div>
                        </div>
                    </div>

                    <!-- SEZIONE 2: METODO DI PAGAMENTO -->
                    <div class="checkout-card">
                        <div class="checkout-card-header">
                            <span class="step-num">2</span>
                            <h3>Metodo di Pagamento</h3>
                        </div>

                        <div class="payment-options-grid">
                            <label class="payment-option-label">
                                <input type="radio" name="metodoPagamento" value="Carta di Credito" checked class="payment-radio">
                                <div class="payment-option-card">
                                    <span class="payment-icon">💳</span>
                                    <strong>Carta di Credito / Debito</strong>
                                    <small>Visa, Mastercard, Maestro, PostePay</small>
                                </div>
                            </label>

                            <label class="payment-option-label">
                                <input type="radio" name="metodoPagamento" value="PayPal" class="payment-radio">
                                <div class="payment-option-card">
                                    <span class="payment-icon">🅿️</span>
                                    <strong>PayPal</strong>
                                    <small>Transazione protetta e immediata</small>
                                </div>
                            </label>

                            <label class="payment-option-label">
                                <input type="radio" name="metodoPagamento" value="Bonifico Bancario" class="payment-radio">
                                <div class="payment-option-card">
                                    <span class="payment-icon">🏦</span>
                                    <strong>Bonifico Bancario Anticipato</strong>
                                    <small>Spedizione dopo accredito</small>
                                </div>
                            </label>
                        </div>

                        <!-- Campi Dati Carta Simulati -->
                        <div class="card-details-box" id="card-details-box">
                            <div class="form-group">
                                <label for="card-number" class="form-label">Numero Carta (16 cifre)</label>
                                <input type="text"
                                       id="card-number"
                                       class="form-control"
                                       placeholder="1234 5678 9012 3456"
                                       maxlength="19">
                                <span class="field-error" id="error-card-number"></span>
                            </div>
                            <div class="form-row-2">
                                <div class="form-group">
                                    <label for="card-expiry" class="form-label">Scadenza (MM/AA)</label>
                                    <input type="text"
                                           id="card-expiry"
                                           class="form-control"
                                           placeholder="12/28"
                                           maxlength="5">
                                    <span class="field-error" id="error-card-expiry"></span>
                                </div>
                                <div class="form-group">
                                    <label for="card-cvv" class="form-label">CVV (3 cifre)</label>
                                    <input type="password"
                                           id="card-cvv"
                                           class="form-control"
                                           placeholder="123"
                                           maxlength="3">
                                    <span class="field-error" id="error-card-cvv"></span>
                                </div>
                            </div>
                        </div>
                    </div>

                </section>

                <!-- COLONNA DESTRA: RIEPILOGO ARTICOLI & TOTALE -->
                <aside class="checkout-summary-section">
                    <div class="summary-card">
                        <h3 class="summary-title">Riepilogo Dispositivi</h3>

                        <div class="checkout-items-list">
                            <c:forEach var="riga" items="${sessionScope.carrello.righe}">
                                <div class="checkout-mini-item">
                                    <div class="mini-item-info">
                                        <span class="mini-item-qty">${riga.quantita}x</span>
                                        <span class="mini-item-name">${riga.prodotto.nome}</span>
                                    </div>
                                    <div class="mini-item-price">
                                        <fmt:formatNumber value="${riga.subtotale}" type="currency" currencySymbol="€" />
                                    </div>
                                </div>
                            </c:forEach>
                        </div>

                        <div class="summary-divider"></div>

                        <div class="summary-line">
                            <span>Subtotale:</span>
                            <span>
                                <fmt:formatNumber value="${sessionScope.carrello.totale}" type="currency" currencySymbol="€" />
                            </span>
                        </div>

                        <div class="summary-line">
                            <span>Spedizione Termocontrollata:</span>
                            <span class="shipping-free">GRATIS</span>
                        </div>

                        <div class="summary-divider"></div>

                        <div class="summary-total-line">
                            <span>Totale da Pagare:</span>
                            <strong class="total-amount">
                                <fmt:formatNumber value="${sessionScope.carrello.totale}" type="currency" currencySymbol="€" />
                            </strong>
                        </div>

                        <button type="submit" class="btn btn-primary btn-block btn-confirm-order" id="btn-submit-order">
                            🔒 Conferma e Paga Ora ➔
                        </button>

                        <div class="security-badge-box">
                            <small>🔒 Transazione protetta con crittografia SSL a 256-bit e certificazione medica CE.</small>
                        </div>
                    </div>
                </aside>

            </div>
        </form>

    </div>
</main>

<jsp:include page="/WEB-INF/view/common/footer.jsp" />
