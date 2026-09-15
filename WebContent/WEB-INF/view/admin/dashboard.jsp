<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Pannello Amministrazione | DiaTech Solutions" scope="request" />
<jsp:include page="/WEB-INF/view/common/header.jsp" />
<jsp:include page="/WEB-INF/view/common/navbar.jsp" />

<main class="main-content admin-page">
    <div class="admin-container">

        <!-- HEADER AMMINISTRAZIONE -->
        <div class="admin-header">
            <div>
                <span class="admin-badge">Area Riservata Operatori</span>
                <h1 class="admin-title">Cruscotto Amministratore</h1>
                <p class="admin-subtitle">Panoramica delle vendite sanitarie, stato del catalogo e monitoraggio ordini.</p>
            </div>
            <div class="admin-quick-actions">
                <a href="${pageContext.request.contextPath}/admin/prodotti" class="btn btn-primary">
                    📦 Gestione Prodotti (CRUD)
                </a>
                <a href="${pageContext.request.contextPath}/admin/ordini" class="btn btn-secondary">
                    📊 Gestione & Report Ordini
                </a>
            </div>
        </div>

        <c:if test="${not empty successMessage}">
            <div class="alert alert-success" role="alert">
                ✅ ${successMessage}
            </div>
        </c:if>

        <!-- KPI / METRICHE GENERALI -->
        <section class="admin-kpi-grid">
            <div class="kpi-card">
                <div class="kpi-icon">📦</div>
                <div class="kpi-info">
                    <span class="kpi-label">Dispositivi a Catalogo</span>
                    <strong class="kpi-value">${totaleProdotti != null ? totaleProdotti : 14}</strong>
                    <small class="kpi-sub">5 categorie gestite</small>
                </div>
            </div>

            <div class="kpi-card">
                <div class="kpi-icon">📋</div>
                <div class="kpi-info">
                    <span class="kpi-label">Ordini Totali Ricevuti</span>
                    <strong class="kpi-value">${totaleOrdini != null ? totaleOrdini : 2}</strong>
                    <small class="kpi-sub">Monitorati in tempo reale</small>
                </div>
            </div>

            <div class="kpi-card">
                <div class="kpi-icon">👥</div>
                <div class="kpi-info">
                    <span class="kpi-label">Pazienti & Clienti</span>
                    <strong class="kpi-value">${totaleClienti != null ? totaleClienti : 2}</strong>
                    <small class="kpi-sub">Utenti registrati attivi</small>
                </div>
            </div>

            <div class="kpi-card">
                <div class="kpi-icon">💶</div>
                <div class="kpi-info">
                    <span class="kpi-label">Volume Vendite</span>
                    <strong class="kpi-value">
                        <fmt:formatNumber value="${volumeVendite != null ? volumeVendite : 143.70}" type="currency" currencySymbol="€" />
                    </strong>
                    <small class="kpi-sub">Fatturato complessivo</small>
                </div>
            </div>
        </section>

        <!-- TABELLA ULTIMI ORDINI RICEVUTI -->
        <section class="admin-recent-orders-section">
            <div class="section-card">
                <div class="section-card-header">
                    <h3>Ultimi Ordini da Lavorare</h3>
                    <a href="${pageContext.request.contextPath}/admin/ordini" class="see-all-link">
                        Vedi tutti gli ordini e filtri ➔
                    </a>
                </div>

                <div class="admin-table-wrapper">
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th>ID Ordine</th>
                                <th>Data e Ora</th>
                                <th>Cliente / Destinatario</th>
                                <th>Stato Ordine</th>
                                <th>Totale</th>
                                <th>Azioni</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty ordiniRecenti}">
                                    <c:forEach var="ord" items="${ordiniRecenti}">
                                        <tr>
                                            <td><strong>#ORD-${ord.id}</strong></td>
                                            <td>${ord.dataOrdine}</td>
                                            <td>${ord.indirizzoSpedizione}, ${ord.citta}</td>
                                            <td>
                                                <span class="status-badge status-${ord.stato}">${ord.stato}</span>
                                            </td>
                                            <td>
                                                <strong>
                                                    <fmt:formatNumber value="${ord.totale}" type="currency" currencySymbol="€" />
                                                </strong>
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/admin/ordini" class="btn btn-outline-sm">
                                                    Dettagli
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="6" class="text-center">Nessun ordine recente da visualizzare.</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>

    </div>
</main>

<jsp:include page="/WEB-INF/view/common/footer.jsp" />
