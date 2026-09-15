<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="Gestione Prodotti (CRUD) | DiaTech Admin" scope="request" />
<jsp:include page="/WEB-INF/view/common/header.jsp" />
<jsp:include page="/WEB-INF/view/common/navbar.jsp" />

<main class="main-content admin-page">
    <div class="admin-container">

        <!-- BREADCRUMB & HEADER -->
        <nav class="breadcrumbs">
            <a href="${pageContext.request.contextPath}/admin/dashboard">Admin Dashboard</a> &rsaquo;
            <span>Gestione Catalogo Prodotti</span>
        </nav>

        <div class="admin-header">
            <div>
                <h1 class="admin-title">Gestione Catalogo Dispositivi (CRUD)</h1>
                <p class="admin-subtitle">Inserisci nuovi dispositivi, aggiorna prezzi e disponibilità, o esegui la cancellazione logica (soft-delete).</p>
            </div>
            <div>
                <a href="#product-form-card" class="btn btn-primary" onclick="resetForm()">
                    ➕ Nuovo Prodotto
                </a>
            </div>
        </div>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger" role="alert">⚠️ ${errorMessage}</div>
        </c:if>
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success" role="alert">✅ ${successMessage}</div>
        </c:if>

        <!-- ================= TABELLA PRODOTTI CATALOGO ================= -->
        <section class="admin-section">
            <div class="section-card">
                <div class="section-card-header">
                    <h3>Elenco Completo Articoli (${prodotti.size()})</h3>
                </div>

                <div class="admin-table-wrapper">
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Foto</th>
                                <th>Nome Dispositivo</th>
                                <th>Categoria</th>
                                <th>Produttore</th>
                                <th>Prezzo</th>
                                <th>Giacenza</th>
                                <th>Stato</th>
                                <th>Azioni</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${prodotti}">
                                <tr class="${p.cancellato ? 'row-deleted' : ''}">
                                    <td><strong>#${p.id}</strong></td>
                                    <td>
                                        <img src="${pageContext.request.contextPath}/images/prodotti/${p.immagine}"
                                             alt="${p.nome}"
                                             class="table-prod-thumb"
                                             onerror="this.src='${pageContext.request.contextPath}/images/prodotti/default.png';">
                                    </td>
                                    <td>
                                        <strong>${p.nome}</strong>
                                    </td>
                                    <td>${p.categoria.nome}</td>
                                    <td>${p.brand.nome}</td>
                                    <td>
                                        <strong>
                                            <fmt:formatNumber value="${p.prezzo}" type="currency" currencySymbol="€" />
                                        </strong>
                                    </td>
                                    <td>
                                        <span class="badge-stock ${p.quantitaDisponibile > 0 ? 'stock-ok' : 'stock-zero'}">
                                            ${p.quantitaDisponibile} pz
                                        </span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${p.cancellato}">
                                                <span class="status-badge status-ANNULLATO" title="Non visibile ai clienti ma preservato negli ordini passati">
                                                    Cancellato (Soft)
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="status-badge status-CONSEGNATO">Attivo</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div class="table-actions">
                                            <button type="button"
                                                    class="btn btn-outline-sm"
                                                    onclick="editProduct(${p.id}, '${p.nome}', '${p.categoria.id}', '${p.brand.id}', ${p.prezzo}, ${p.quantitaDisponibile}, '${p.immagine}', '${p.descrizione}')">
                                                ✏️ Modifica
                                            </button>
                                            <c:if test="${!p.cancellato}">
                                                <a href="${pageContext.request.contextPath}/admin/prodotti?action=delete&id=${p.id}"
                                                   class="btn btn-danger-outline-sm"
                                                   onclick="return confirm('Vuoi cancellare logicamente ${p.nome}? Rimarrà intatto nei vecchi ordini.');">
                                                    🗑️ Elimina
                                                </a>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>

        <!-- ================= FORM INSERIMENTO / MODIFICA PRODOTTO ================= -->
        <section class="admin-section" id="product-form-card">
            <div class="section-card">
                <div class="section-card-header">
                    <h3 id="form-card-title">Inserisci Nuovo Dispositivo Medico</h3>
                </div>

                <form action="${pageContext.request.contextPath}/admin/prodotti" method="POST" class="admin-form" id="admin-product-form">
                    <input type="hidden" name="action" id="form-action" value="save">
                    <input type="hidden" name="id" id="prod-id" value="">

                    <div class="form-row-2">
                        <div class="form-group">
                            <label for="prod-nome" class="form-label">Nome Dispositivo *</label>
                            <input type="text" id="prod-nome" name="nome" class="form-control" placeholder="Es. FreeStyle Libre 3" required>
                        </div>
                        <div class="form-group">
                            <label for="prod-immagine" class="form-label">Nome File Immagine</label>
                            <input type="text" id="prod-immagine" name="immagine" class="form-control" placeholder="Es. freestyle_libre3.png">
                        </div>
                    </div>

                    <div class="form-row-4">
                        <div class="form-group">
                            <label for="prod-categoria" class="form-label">Categoria *</label>
                            <select id="prod-categoria" name="idCategoria" class="form-control" required>
                                <c:forEach var="cat" items="${categorie}">
                                    <option value="${cat.id}">${cat.nome}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="prod-brand" class="form-label">Produttore (Brand) *</label>
                            <select id="prod-brand" name="idBrand" class="form-control" required>
                                <c:forEach var="br" items="${brands}">
                                    <option value="${br.id}">${br.nome}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="prod-prezzo" class="form-label">Prezzo (€) *</label>
                            <input type="number" id="prod-prezzo" name="prezzo" class="form-control" step="0.01" min="0" placeholder="59.90" required>
                        </div>

                        <div class="form-group">
                            <label for="prod-quantita" class="form-label">Giacenza Magazzino *</label>
                            <input type="number" id="prod-quantita" name="quantita" class="form-control" min="0" placeholder="50" required>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="prod-descrizione" class="form-label">Descrizione Medica / Scheda Tecnica</label>
                        <textarea id="prod-descrizione" name="descrizione" rows="4" class="form-control" placeholder="Specifiche tecniche, durata del sensore, accuratezza MARD, compatibilità..."></textarea>
                    </div>

                    <div class="form-actions-group">
                        <button type="submit" class="btn btn-primary" id="btn-save-product">
                            💾 Salva Dispositivo nel Catalogo
                        </button>
                        <button type="button" class="btn btn-outline" onclick="resetForm()">
                            Annulla / Svuota Form
                        </button>
                    </div>
                </form>
            </div>
        </section>

    </div>
</main>

<script>
    function editProduct(id, nome, idCat, idBrand, prezzo, quantita, immagine, descrizione) {
        document.getElementById('form-card-title').innerText = 'Modifica Dispositivo #' + id + ' - ' + nome;
        document.getElementById('form-action').value = 'update';
        document.getElementById('prod-id').value = id;
        document.getElementById('prod-nome').value = nome;
        document.getElementById('prod-categoria').value = idCat;
        document.getElementById('prod-brand').value = idBrand;
        document.getElementById('prod-prezzo').value = prezzo;
        document.getElementById('prod-quantita').value = quantita;
        document.getElementById('prod-immagine').value = immagine;
        document.getElementById('prod-descrizione').value = descrizione;
        document.getElementById('btn-save-product').innerText = '🔄 Aggiorna Dispositivo';

        document.getElementById('product-form-card').scrollIntoView({ behavior: 'smooth' });
    }

    function resetForm() {
        document.getElementById('form-card-title').innerText = 'Inserisci Nuovo Dispositivo Medico';
        document.getElementById('form-action').value = 'save';
        document.getElementById('prod-id').value = '';
        document.getElementById('admin-product-form').reset();
        document.getElementById('btn-save-product').innerText = '💾 Salva Dispositivo nel Catalogo';
    }
</script>

<jsp:include page="/WEB-INF/view/common/footer.jsp" />
