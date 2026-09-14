<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:set var="pageTitle" value="DiaTech Solutions | Accedi o Registrati" scope="request" />
<c:set var="extraJs" value="validazione.js" scope="request" />
<jsp:include page="/WEB-INF/view/common/header.jsp" />
<jsp:include page="/WEB-INF/view/common/navbar.jsp" />

<main class="main-content auth-page">
    <div class="auth-container">

        <!-- MESSAGGI DI NOTIFICA / ERRORE SERVER -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger" role="alert">
                ⚠️ ${errorMessage}
            </div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success" role="alert">
                ✅ ${successMessage}
            </div>
        </c:if>
        <c:if test="${param.error == 'unauthorized_admin'}">
            <div class="alert alert-warning" role="alert">
                🔒 Accesso riservato: è necessario autenticarsi con un account Amministratore.
            </div>
        </c:if>

        <div class="auth-grid">

            <!-- ================= FORM DI LOGIN ================= -->
            <section class="auth-box login-box">
                <div class="auth-box-header">
                    <h2>Accedi al tuo Account</h2>
                    <p>Inserisci le tue credenziali per accedere agli ordini e al carrello salvato.</p>
                </div>

                <form action="${pageContext.request.contextPath}/login" method="POST" class="auth-form" id="form-login" novalidate>
                    <!-- Parametro di reindirizzamento (es. dopo il blocco di AuthFilter) -->
                    <c:if test="${not empty param.redirect}">
                        <input type="hidden" name="redirect" value="${param.redirect}">
                    </c:if>

                    <div class="form-group">
                        <label for="login-email" class="form-label">Indirizzo Email *</label>
                        <input type="email"
                               id="login-email"
                               name="email"
                               class="form-control"
                               placeholder="nome@esempio.it"
                               required
                               autocomplete="email">
                        <span class="field-error" id="error-login-email"></span>
                    </div>

                    <div class="form-group">
                        <label for="login-password" class="form-label">Password *</label>
                        <input type="password"
                               id="login-password"
                               name="password"
                               class="form-control"
                               placeholder="••••••••"
                               required
                               autocomplete="current-password">
                        <span class="field-error" id="error-login-password"></span>
                    </div>

                    <button type="submit" class="btn btn-primary btn-block btn-auth" id="btn-submit-login">
                        Accedi ➔
                    </button>
                </form>

                <div class="demo-credentials-box">
                    <small>
                        <strong>Account di prova disponibili:</strong><br>
                        • Admin: <code>admin@diatech.it</code> / <code>Admin123!</code><br>
                        • Cliente: <code>mario.rossi@email.it</code> / <code>User123!</code>
                    </small>
                </div>
            </section>

            <!-- ================= FORM DI REGISTRAZIONE ================= -->
            <section class="auth-box register-box">
                <div class="auth-box-header">
                    <h2>Nuovo Paziente / Caregiver?</h2>
                    <p>Registrati gratuitamente per completare ordini e usufruire delle garanzie sanitarie.</p>
                </div>

                <form action="${pageContext.request.contextPath}/registrazione" method="POST" class="auth-form" id="form-register" novalidate>
                    <div class="form-row-2">
                        <div class="form-group">
                            <label for="reg-nome" class="form-label">Nome *</label>
                            <input type="text"
                                   id="reg-nome"
                                   name="nome"
                                   class="form-control"
                                   placeholder="Mario"
                                   required>
                            <span class="field-error" id="error-reg-nome"></span>
                        </div>
                        <div class="form-group">
                            <label for="reg-cognome" class="form-label">Cognome *</label>
                            <input type="text"
                                   id="reg-cognome"
                                   name="cognome"
                                   class="form-control"
                                   placeholder="Rossi"
                                   required>
                            <span class="field-error" id="error-reg-cognome"></span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="reg-email" class="form-label">Indirizzo Email *</label>
                        <input type="email"
                               id="reg-email"
                               name="email"
                               class="form-control"
                               placeholder="mario.rossi@email.it"
                               required
                               autocomplete="email">
                        <!-- Errore DOM e feedback AJAX unicità email -->
                        <span class="field-error" id="error-reg-email"></span>
                    </div>

                    <div class="form-row-2">
                        <div class="form-group">
                            <label for="reg-password" class="form-label">Password * <small>(min. 8 car.)</small></label>
                            <input type="password"
                                   id="reg-password"
                                   name="password"
                                   class="form-control"
                                   placeholder="Almeno 8 caratteri"
                                   required
                                   autocomplete="new-password">
                            <span class="field-error" id="error-reg-password"></span>
                        </div>
                        <div class="form-group">
                            <label for="reg-conferma-password" class="form-label">Conferma Password *</label>
                            <input type="password"
                                   id="reg-conferma-password"
                                   name="confermaPassword"
                                   class="form-control"
                                   placeholder="Ripeti password"
                                   required
                                   autocomplete="new-password">
                            <span class="field-error" id="error-reg-conferma-password"></span>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="reg-indirizzo" class="form-label">Indirizzo di Residenza</label>
                        <input type="text"
                               id="reg-indirizzo"
                               name="indirizzo"
                               class="form-control"
                               placeholder="Via/Piazza e Numero Civico">
                        <span class="field-error" id="error-reg-indirizzo"></span>
                    </div>

                    <div class="form-row-3">
                        <div class="form-group flex-2">
                            <label for="reg-citta" class="form-label">Città</label>
                            <input type="text"
                                   id="reg-citta"
                                   name="citta"
                                   class="form-control"
                                   placeholder="Salerno">
                            <span class="field-error" id="error-reg-citta"></span>
                        </div>
                        <div class="form-group flex-1">
                            <label for="reg-cap" class="form-label">CAP</label>
                            <input type="text"
                                   id="reg-cap"
                                   name="cap"
                                   class="form-control"
                                   placeholder="84100"
                                   maxlength="5">
                            <span class="field-error" id="error-reg-cap"></span>
                        </div>
                        <div class="form-group flex-2">
                            <label for="reg-telefono" class="form-label">Telefono</label>
                            <input type="tel"
                                   id="reg-telefono"
                                   name="telefono"
                                   class="form-control"
                                   placeholder="3331234567">
                            <span class="field-error" id="error-reg-telefono"></span>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-secondary btn-block btn-auth" id="btn-submit-register">
                        Registrati come Cliente ➔
                    </button>
                </form>
            </section>

        </div>
    </div>
</main>

<jsp:include page="/WEB-INF/view/common/footer.jsp" />
