<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pageTitle" value="${prodotto.nome} | DiaTech Solutions" scope="request" />
<jsp:include page="/WEB-INF/view/common/header.jsp" />
<jsp:include page="/WEB-INF/view/common/navbar.jsp" />

<main class="main-content product-detail-page">
    <div class="product-detail-container">

        <!-- BREADCRUMBS -->
        <nav class="breadcrumbs">
            <a href="${pageContext.request.contextPath}/home">Home</a> &rsaquo;
            <a href="${pageContext.request.contextPath}/catalogo">Catalogo</a> &rsaquo;
            <a href="${pageContext.request.contextPath}/catalogo?categoria=${prodotto.categoria.id}">${prodotto.categoria.nome}</a> &rsaquo;
            <span>${prodotto.nome}</span>
        </nav>

        <c:choose>
            <c:when test="${not empty prodotto}">
                <div class="product-main-card">
                    <!-- Colonna Sinistra: Immagine Prodotto -->
                    <div class="product-gallery">
                        <div class="main-image-wrapper">
                            <img src="${pageContext.request.contextPath}/images/prodotti/${prodotto.immagine}"
                                 alt="${prodotto.nome}"
                                 class="detail-product-img"
                                 onerror="this.src='${pageContext.request.contextPath}/images/prodotti/default.png';">
                        </div>
                    </div>

                    <!-- Colonna Destra: Dettagli e Acquisto -->
                    <div class="product-summary">
                        <div class="product-meta-header">
                            <span class="badge-brand-lg">${prodotto.brand.nome}</span>
                            <span class="badge-category-lg">${prodotto.categoria.nome}</span>
                        </div>

                        <h1 class="detail-title">${prodotto.nome}</h1>

                        <div class="detail-price-box">
                            <span class="price-value">
                                <fmt:formatNumber value="${prodotto.prezzo}" type="currency" currencySymbol="€" />
                            </span>
                            <span class="vat-included">IVA inclusa</span>
                        </div>

                        <div class="stock-availability ${prodotto.quantitaDisponibile > 0 ? 'available' : 'unavailable'}">
                            <c:choose>
                                <c:when test="${prodotto.quantitaDisponibile > 0}">
                                    <span class="status-indicator in-stock"></span>
                                    <strong>Disponibilità Immediata:</strong> ${prodotto.quantitaDisponibile} unità a magazzino
                                </c:when>
                                <c:otherwise>
                                    <span class="status-indicator out-of-stock"></span>
                                    <strong>Attualmente Esaurito:</strong> Riordino in corso
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="detail-description">
                            <h3>Descrizione del Dispositivo</h3>
                            <p>${prodotto.descrizione}</p>
                        </div>

                        <!-- Form di Aggiunta al Carrello con Selettore Quantità -->
                        <c:if test="${prodotto.quantitaDisponibile > 0}">
                            <form action="${pageContext.request.contextPath}/carrello" method="POST" class="add-to-cart-form" id="detail-add-cart-form">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="idProdotto" value="${prodotto.id}">

                                <div class="quantity-selector-group">
                                    <label for="quantita-input" class="qty-label">Quantità:</label>
                                    <div class="qty-stepper">
                                        <button type="button" class="btn-qty btn-minus" id="btn-qty-minus">-</button>
                                        <input type="number"
                                               name="quantita"
                                               id="quantita-input"
                                               value="1"
                                               min="1"
                                               max="${prodotto.quantitaDisponibile}"
                                               class="qty-input">
                                        <button type="button" class="btn-qty btn-plus" id="btn-qty-plus">+</button>
                                    </div>
                                </div>

                                <button type="button"
                                        class="btn btn-primary btn-add-cart-lg btn-add-cart-ajax"
                                        data-id="${prodotto.id}"
                                        data-nome="${prodotto.nome}">
                                    🛒 Aggiungi al Carrello
                                </button>
                            </form>
                        </c:if>

                        <!-- Box Garanzie e Certificazioni -->
                        <div class="product-guarantees-box">
                            <div class="guarantee-item">
                                <span class="g-icon">🛡️</span>
                                <div class="g-text">
                                    <strong>Garanzia Legale di 2 Anni</strong>
                                    <p>Certificato automatico generato e consultabile nello storico ordini.</p>
                                </div>
                            </div>
                            <div class="guarantee-item">
                                <span class="g-icon">🩺</span>
                                <div class="g-text">
                                    <strong>Dispositivo Medico Certificato CE</strong>
                                    <p>Conforme alle direttive europee per la gestione del diabete.</p>
                                </div>
                            </div>
                            <div class="guarantee-item">
                                <span class="g-icon">❄️</span>
                                <div class="g-text">
                                    <strong>Conservazione Termica Garantita</strong>
                                    <p>Stoccaggio a temperatura controllata per la massima affidabilità.</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- SEZIONE PRODOTTI COMPATIBILI ED ACCESSORI -->
                <c:if test="${not empty prodottiCompatibili}">
                    <section class="compatible-products-section">
                        <div class="section-header">
                            <h2>Prodotti ed Accessori Compatibili</h2>
                            <p>Consigliati per l'utilizzo ottimale con <strong>${prodotto.nome}</strong></p>
                        </div>

                        <div class="compatible-grid">
                            <c:forEach var="comp" items="${prodottiCompatibili}">
                                <div class="compatible-card">
                                    <a href="${pageContext.request.contextPath}/prodotto?id=${comp.id}" class="comp-img-link">
                                        <img src="${pageContext.request.contextPath}/images/prodotti/${comp.immagine}"
                                             alt="${comp.nome}"
                                             class="comp-img"
                                             onerror="this.src='${pageContext.request.contextPath}/images/prodotti/default.png';">
                                    </a>
                                    <div class="comp-info">
                                        <span class="comp-category">${comp.categoria.nome}</span>
                                        <h4 class="comp-title">
                                            <a href="${pageContext.request.contextPath}/prodotto?id=${comp.id}">${comp.nome}</a>
                                        </h4>
                                        <div class="comp-price">
                                            <fmt:formatNumber value="${comp.prezzo}" type="currency" currencySymbol="€" />
                                        </div>
                                        <button type="button"
                                                class="btn btn-outline-sm btn-add-cart-ajax"
                                                data-id="${comp.id}"
                                                data-nome="${comp.nome}">
                                            + Aggiungi
                                        </button>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </section>
                </c:if>
            </c:when>
            <c:otherwise>
                <div class="product-not-found">
                    <h2>Prodotto Non Trovato</h2>
                    <p>Il dispositivo medico richiesto non esiste o è stato rimosso dal catalogo.</p>
                    <a href="${pageContext.request.contextPath}/catalogo" class="btn btn-primary">
                        Torna al Catalogo Prodotti
                    </a>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</main>

<jsp:include page="/WEB-INF/view/common/footer.jsp" />
