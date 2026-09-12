<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="DiaTech Solutions | Home - Dispositivi Medici per il Diabete" scope="request" />
<jsp:include page="/WEB-INF/view/common/header.jsp" />
<jsp:include page="/WEB-INF/view/common/navbar.jsp" />

<main class="main-content home-page">

    <!-- HERO BANNER -->
    <section class="hero-section">
        <div class="hero-container">
            <div class="hero-content">
                <span class="hero-badge">Tecnologia & Salute</span>
                <h1 class="hero-title">Il Futuro del Monitoraggio Glicemico è Qui</h1>
                <p class="hero-subtitle">
                    Sensori CGM continui, glucometri digitali di ultima generazione e accessori per la gestione quotidiana del diabete con la massima precisione e serenità.
                </p>
                <div class="hero-buttons">
                    <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-primary">
                        Esplora il Catalogo ➔
                    </a>
                    <a href="${pageContext.request.contextPath}/catalogo?categoria=1" class="btn btn-secondary">
                        Sensori CGM
                    </a>
                </div>
            </div>
            <div class="hero-image">
                <img src="${pageContext.request.contextPath}/images/prodotti/freestyle_libre3.png"
                     alt="Sensore CGM FreeStyle Libre 3"
                     class="hero-product-img"
                     onerror="this.src='${pageContext.request.contextPath}/images/prodotti/default.png';">
            </div>
        </div>
    </section>

    <!-- CARTELLI CATEGORIE RAPIDE -->
    <section class="categories-section">
        <div class="section-container">
            <div class="section-header">
                <h2 class="section-title">Categorie Principali</h2>
                <p class="section-subtitle">Trova rapidamente i dispositivi e i consumabili di cui hai bisogno</p>
            </div>

            <div class="categories-grid">
                <a href="${pageContext.request.contextPath}/catalogo?categoria=1" class="category-card">
                    <div class="category-icon">📡</div>
                    <h3>Sensori CGM</h3>
                    <p>Monitoraggio continuo senza punture</p>
                </a>
                <a href="${pageContext.request.contextPath}/catalogo?categoria=2" class="category-card">
                    <div class="category-icon">📟</div>
                    <h3>Glucometri</h3>
                    <p>Misuratori digitali di precisione</p>
                </a>
                <a href="${pageContext.request.contextPath}/catalogo?categoria=3" class="category-card">
                    <div class="category-icon">🏷️</div>
                    <h3>Strisce Reattive</h3>
                    <p>Consumabili originali certificati</p>
                </a>
                <a href="${pageContext.request.contextPath}/catalogo?categoria=4" class="category-card">
                    <div class="category-icon">💉</div>
                    <h3>Pungidito e Aghi</h3>
                    <p>Lancette sterili atraumatiche</p>
                </a>
                <a href="${pageContext.request.contextPath}/catalogo?categoria=5" class="category-card">
                    <div class="category-icon">🛡️</div>
                    <h3>Accessori & Patch</h3>
                    <p>Custodie rigide e cerotti protettivi</p>
                </a>
            </div>
        </div>
    </section>

    <!-- PRODOTTI IN EVIDENZA -->
    <section class="featured-products-section">
        <div class="section-container">
            <div class="section-header">
                <h2 class="section-title">Prodotti in Evidenza</h2>
                <p class="section-subtitle">I dispositivi più scelti dai nostri pazienti e specialisti</p>
            </div>

            <div class="products-grid">
                <c:choose>
                    <c:when test="${not empty prodottiInEvidenza}">
                        <c:forEach var="prodotto" items="${prodottiInEvidenza}">
                            <div class="product-card">
                                <!-- Badge Categoria/Brand -->
                                <div class="product-badges">
                                    <span class="badge-brand">${prodotto.brand.nome}</span>
                                    <span class="badge-category">${prodotto.categoria.nome}</span>
                                </div>

                                <!-- Immagine Prodotto con Link -->
                                <a href="${pageContext.request.contextPath}/prodotto?id=${prodotto.id}" class="product-img-wrapper">
                                    <img src="${pageContext.request.contextPath}/images/prodotti/${prodotto.immagine}"
                                         alt="${prodotto.nome}"
                                         class="product-img"
                                         onerror="this.src='${pageContext.request.contextPath}/images/prodotti/default.png';">
                                </a>

                                <!-- Info Prodotto -->
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

                                    <!-- Azione Aggiungi al Carrello AJAX -->
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
                        <p class="no-products-msg">Nessun prodotto in evidenza disponibile al momento.</p>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="section-footer-cta">
                <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-outline">
                    Vedi Tutti i Prodotti del Catalogo ➔
                </a>
            </div>
        </div>
    </section>

    <!-- BANNER GARANZIE E SERVIZI -->
    <section class="trust-features-section" id="servizi">
        <div class="section-container">
            <div class="features-grid">
                <div class="feature-box">
                    <div class="feature-icon">🛡️</div>
                    <h4>Garanzia Legale 2 Anni</h4>
                    <p>Copertura completa e tracciabilità del certificato su ogni acquisto</p>
                </div>
                <div class="feature-box" id="spedizioni">
                    <div class="feature-icon">📦</div>
                    <h4>Spedizioni Termocontrollate</h4>
                    <p>I sensori viaggiano a temperatura controllata per preservare gli enzimi</p>
                </div>
                <div class="feature-box">
                    <div class="feature-icon">🔒</div>
                    <h4>Pagamenti Sicuri</h4>
                    <p>Transazioni protette con crittografia SSL e standard di sicurezza bancari</p>
                </div>
                <div class="feature-box">
                    <div class="feature-icon">🩺</div>
                    <h4>Dispositivi Certificati CE</h4>
                    <p>Solo elettromedicali e reagenti conformi alle normative del Ministero della Salute</p>
                </div>
            </div>
        </div>
    </section>

</main>

<jsp:include page="/WEB-INF/view/common/footer.jsp" />
