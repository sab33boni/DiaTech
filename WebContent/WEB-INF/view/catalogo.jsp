<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="DiaTech Solutions | Catalogo Prodotti e Dispositivi Medici" scope="request" />
<jsp:include page="/WEB-INF/view/common/header.jsp" />
<jsp:include page="/WEB-INF/view/common/navbar.jsp" />

<main class="main-content catalog-page">
    <div class="catalog-layout-container">

        <!-- SIDEBAR DEI FILTRI DI RICERCA -->
        <aside class="catalog-sidebar">
            <div class="filter-card">
                <div class="filter-header">
                    <h3>Filtri di Ricerca</h3>
                    <a href="${pageContext.request.contextPath}/catalogo" class="filter-reset-link" title="Azzera tutti i filtri">
                        Azzera
                    </a>
                </div>

                <form action="${pageContext.request.contextPath}/catalogo" method="GET" class="filter-form" id="catalog-filter-form">
                    <!-- Mantiene la parola chiave di ricerca se presente -->
                    <c:if test="${not empty param.q}">
                        <input type="hidden" name="q" value="${param.q}">
                    </c:if>

                    <!-- Filtro per Categoria -->
                    <div class="filter-group">
                        <label for="filter-categoria" class="filter-label">Categoria</label>
                        <select name="categoria" id="filter-categoria" class="filter-select">
                            <option value="">Tutte le Categorie</option>
                            <c:forEach var="cat" items="${categorie}">
                                <option value="${cat.id}" ${param.categoria == cat.id ? 'selected' : ''}>
                                    ${cat.nome}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Filtro per Produttore / Brand -->
                    <div class="filter-group">
                        <label for="filter-brand" class="filter-label">Produttore (Brand)</label>
                        <select name="brand" id="filter-brand" class="filter-select">
                            <option value="">Tutti i Produttori</option>
                            <c:forEach var="br" items="${brands}">
                                <option value="${br.id}" ${param.brand == br.id ? 'selected' : ''}>
                                    ${br.nome}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Filtro per Fascia di Prezzo -->
                    <div class="filter-group">
                        <label class="filter-label">Fascia di Prezzo (€)</label>
                        <div class="price-inputs">
                            <input type="number"
                                   name="minPrezzo"
                                   placeholder="Min"
                                   min="0"
                                   step="0.5"
                                   class="filter-input"
                                   value="${param.minPrezzo}">
                            <span class="price-separator">-</span>
                            <input type="number"
                                   name="maxPrezzo"
                                   placeholder="Max"
                                   min="0"
                                   step="0.5"
                                   class="filter-input"
                                   value="${param.maxPrezzo}">
                        </div>
                    </div>

                    <!-- Ordinamento -->
                    <div class="filter-group">
                        <label for="filter-order" class="filter-label">Ordina Per</label>
                        <select name="order" id="filter-order" class="filter-select">
                            <option value="id_asc" ${param.order == 'id_asc' ? 'selected' : ''}>Predefinito</option>
                            <option value="prezzo_asc" ${param.order == 'prezzo_asc' ? 'selected' : ''}>Prezzo: dal più basso</option>
                            <option value="prezzo_desc" ${param.order == 'prezzo_desc' ? 'selected' : ''}>Prezzo: dal più alto</option>
                            <option value="nome_asc" ${param.order == 'nome_asc' ? 'selected' : ''}>Nome: A-Z</option>
                            <option value="nome_desc" ${param.order == 'nome_desc' ? 'selected' : ''}>Nome: Z-A</option>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-primary btn-block filter-submit-btn">
                        Applica Filtri ➔
                    </button>
                </form>
            </div>
        </aside>

        <!-- AREA PRINCIPALE: GRIGLIA PRODOTTI CATALOGO -->
        <section class="catalog-main-content">
            <div class="catalog-results-header">
                <div class="results-info">
                    <h1 class="catalog-title">
                        <c:choose>
                            <c:when test="${not empty param.q}">
                                Risultati di ricerca per: <em>"${param.q}"</em>
                            </c:when>
                            <c:otherwise>
                                Catalogo Dispositivi & Consumabili
                            </c:otherwise>
                        </c:choose>
                    </h1>
                    <p class="results-count">
                        <c:choose>
                            <c:when test="${not empty prodotti}">
                                Mostrando <strong>${prodotti.size()}</strong> prodotti disponibili
                            </c:when>
                            <c:otherwise>
                                0 prodotti trovati
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>

            <!-- Griglia dei Prodotti -->
            <div class="products-grid">
                <c:choose>
                    <c:when test="${not empty prodotti}">
                        <c:forEach var="prodotto" items="${prodotti}">
                            <div class="product-card">
                                <div class="product-badges">
                                    <span class="badge-brand">${prodotto.brand.nome}</span>
                                    <span class="badge-category">${prodotto.categoria.nome}</span>
                                </div>

                                <a href="${pageContext.request.contextPath}/prodotto?id=${prodotto.id}" class="product-img-wrapper">
                                    <img src="${pageContext.request.contextPath}/images/prodotti/${prodotto.immagine}"
                                         alt="${prodotto.nome}"
                                         class="product-img"
                                         onerror="this.src='${pageContext.request.contextPath}/images/prodotti/default.png';">
                                </a>

                                <div class="product-info">
                                    <h3 class="product-title">
                                        <a href="${pageContext.request.contextPath}/prodotto?id=${prodotto.id}">
                                            ${prodotto.nome}
                                        </a>
                                    </h3>
                                    <p class="product-description">${prodotto.descrizione}</p>

                                    <div class="product-meta">
                                        <div class="product-price">
                                            <fmt:formatNumber value="${prodotto.prezzo}" type="currency" currencySymbol="€" />
                                        </div>
                                        <div class="product-stock ${prodotto.quantitaDisponibile > 0 ? 'in-stock' : 'out-of-stock'}">
                                            <c:choose>
                                                <c:when test="${prodotto.quantitaDisponibile > 0}">
                                                    <span class="stock-dot"></span> Disponibile
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="stock-dot out"></span> Esaurito
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <div class="product-actions">
                                        <c:choose>
                                            <c:when test="${prodotto.quantitaDisponibile > 0}">
                                                <button type="button"
                                                        class="btn btn-primary btn-add-cart-ajax"
                                                        data-id="${prodotto.id}"
                                                        data-nome="${prodotto.nome}">
                                                    🛒 Aggiungi al Carrello
                                                </button>
                                            </c:when>
                                            <c:otherwise>
                                                <button type="button" class="btn btn-disabled" disabled>
                                                    Non Disponibile
                                                </button>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="no-results-box">
                            <span class="no-results-icon">🔍</span>
                            <h3>Nessun prodotto trovato</h3>
                            <p>Prova a modificare i filtri di ricerca, la parola chiave o il range di prezzo.</p>
                            <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-secondary">
                                Mostra Tutti i Prodotti
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>

    </div>
</main>

<jsp:include page="/WEB-INF/view/common/footer.jsp" />
