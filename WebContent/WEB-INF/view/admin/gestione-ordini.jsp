<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Gestione & Report Ordini | DiaTech Admin" scope="request" />
<jsp:include page="/WEB-INF/view/common/header.jsp" />
<jsp:include page="/WEB-INF/view/common/navbar.jsp" />

<main class="main-content admin-page">
    <div class="admin-container">

        <!-- BREADCRUMBS & HEADER -->
        <nav class="breadcrumbs">
            <a href="${pageContext.request.contextPath}/admin/dashboard">Admin Dashboard</a> &rsaquo;
            <span>Gestione e Reportistica Ordini</span>
        </nav>

        <div class="admin-header">
            <div>
                <h1 class="admin-title">Gestione Ordini & Report Vendite</h1>
                <p class="admin-subtitle">Filtra le transazioni per intervallo temporale o per singolo cliente e aggiorna lo stato di avanzamento.</p>
            </div>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger" role="alert">⚠️ ${errorMessage}</div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success" role="alert">✅ ${successMessage}</div>
        </c:if>

        <!-- ================= BARRA DEI FILTRI (DATE & CLIENTE) ================= -->
        <section class="admin-filter-card">
            <form action="${pageContext.request.contextPath}/admin/ordini" method="GET" class="admin-filter-form">
                <div class="filter-row">
                    <!-- Filtro Data Inizio -->
                    <div class="filter-field">
                        <label for="startDate" class="filter-label">Da Data:</label>
                        <input type="date"
                               id="startDate"
                               name="startDate"
                               class="form-control"
                               value="${param.startDate}">
                    </div>

                    <!-- Filtro Data Fine -->
                    <div class="filter-field">
                        <label for="endDate" class="filter-label">A Data:</label>
                        <input type="date"
                               id="endDate"
                               name="endDate"
                               class="form-control"
                               value="${param.endDate}">
                    </div>

                    <!-- Filtro per Cliente Registrato -->
                    <div class="filter-field flex-grow">
                        <label for="idCliente" class="filter-label">Filtra per Paziente / Cliente:</label>
                        <select id="idCliente" name="idCliente" class="form-control">
                            <option value="">Tutti i Clienti Registrati</option>
                            <c:forEach var="cl" items="${clienti}">
                                <option value="${cl.id}" ${param.idCliente == cl.id ? 'selected' : ''}>
                                    ${cl.cognome} ${cl.nome} (${cl.email})
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Pulsanti Azione Filtro -->
                    <div class="filter-actions">
                        <button type="submit" class="btn btn-primary">
                            🔍 Filtra
                        </button>
                        <a href="${pageContext.request.contextPath}/admin/ordini" class="btn btn-outline">
                            Azzera
                        </a>
                    </div>
                </div>
            </form>
        </section>

        <!-- ================= TABELLA DEGLI ORDINI ================= -->
        <section class="admin-section">
            <div class="section-card">
                <div class="section-card-header">
                    <h3>Ordini Trovati (${ordini.size()})</h3>
                </div>

                <div class="admin-orders-table-wrapper">
                    <c:choose>
                        <c:when test="${not empty ordini}">
                            <table class="admin-table admin-orders-table">
                                <thead>
                                    <tr>
                                        <th>ID Ordine</th>
                                        <th>Data & Ora</th>
                                        <th>Spedizione & Recapito</th>
                                        <th>Dispositivi Acquistati (Prezzo Congelato)</th>
                                        <th>Totale</th>
                                        <th>Stato Attuale</th>
                                        <th>Aggiorna Stato</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="ord" items="${ordini}">
                                        <tr>
                                            <!-- ID Ordine -->
                                            <td><strong>#ORD-${ord.id}</strong></td>

                                            <!-- Data -->
                                            <td>
                                                <small>${ord.dataOrdine}</small>
                                            </td>

                                            <!-- Indirizzo e Pagamento -->
                                            <td>
                                                <strong>${ord.indirizzoSpedizione}</strong><br>
                                                <small>${ord.cap} ${ord.citta}</small><br>
                                                <small class="badge-payment">💳 ${ord.metodoPagamento}</small>
                                            </td>

                                            <!-- Righe d'Ordine con Prezzo Congelato -->
                                            <td class="td-order-items-mini">
                                                <ul class="mini-order-items-list">
                                                    <c:forEach var="riga" items="${ord.righe}">
                                                        <li>
                                                            <strong>${riga.quantita}x</strong> ${riga.prodotto.nome}
                                                            <span class="price-tag">
                                                                (<fmt:formatNumber value="${riga.prezzoUnitario}" type="currency" currencySymbol="€" />)
                                                            </span>
                                                        </li>
                                                    </c:forEach>
                                                </ul>
                                            </td>

                                            <!-- Totale Ordine -->
                                            <td>
                                                <strong class="order-total-highlight">
                                                    <fmt:formatNumber value="${ord.totale}" type="currency" currencySymbol="€" />
                                                </strong>
                                            </td>

                                            <!-- Stato Badge -->
                                            <td>
                                                <span class="status-badge status-${ord.stato}">
                                                    ${ord.stato}
                                                </span>
                                            </td>

                                            <!-- Form per Cambio Stato Rapido -->
                                            <td>
                                                <form action="${pageContext.request.contextPath}/admin/ordini" method="POST" class="form-change-status">
                                                    <input type="hidden" name="action" value="updateStatus">
                                                    <input type="hidden" name="idOrdine" value="${ord.id}">

                                                    <select name="nuovoStato" class="status-select">
                                                        <option value="IN_LAVORAZIONE" ${ord.stato == 'IN_LAVORAZIONE' ? 'selected' : ''}>IN_LAVORAZIONE</option>
                                                        <option value="SPEDITO" ${ord.stato == 'SPEDITO' ? 'selected' : ''}>SPEDITO</option>
                                                        <option value="CONSEGNATO" ${ord.stato == 'CONSEGNATO' ? 'selected' : ''}>CONSEGNATO</option>
                                                        <option value="ANNULLATO" ${ord.stato == 'ANNULLATO' ? 'selected' : ''}>ANNULLATO</option>
                                                    </select>

                                                    <button type="submit" class="btn btn-outline-sm btn-save-status" title="Salva nuovo stato">
                                                        💾
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:when>
                        <c:otherwise>
                            <div class="no-results-box">
                                <span class="no-results-icon">📋</span>
                                <h3>Nessun ordine trovato con i criteri selezionati</h3>
                                <p>Prova a modificare l'intervallo temporale o a selezionare un cliente diverso.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </section>

    </div>
</main>

<jsp:include page="/WEB-INF/view/common/footer.jsp" />
