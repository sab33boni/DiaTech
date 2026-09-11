<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<header class="site-header">
    <div class="header-container">
        <!-- Logo e Titolo Brand -->
        <div class="brand-logo">
            <a href="${pageContext.request.contextPath}/home" class="logo-link">
                <span class="logo-icon">🩺</span>
                <span class="logo-text">Dia<strong>Tech</strong> <small>Solutions</small></span>
            </a>
        </div>

        <!-- Barra di Ricerca Live AJAX con tendina suggerimenti -->
        <div class="search-bar-container">
            <form action="${pageContext.request.contextPath}/catalogo" method="GET" class="search-form" id="search-form">
                <input type="text"
                       name="q"
                       id="live-search-input"
                       class="search-input"
                       placeholder="Cerca sensori CGM, glucometri, strisce..."
                       autocomplete="off"
                       value="${param.q}">
                <button type="submit" class="search-button" aria-label="Cerca">🔍</button>
            </form>
            <!-- Dropdown popolato dinamicamente via AJAX dallo script live-search.js -->
            <div id="live-search-results" class="search-results-dropdown" style="display: none;"></div>
        </div>

        <!-- Azioni Utente: Carrello e Autenticazione -->
        <div class="header-actions">
            <!-- Pulsante Carrello con Badge Conteggio Articoli -->
            <a href="${pageContext.request.contextPath}/carrello" class="cart-link" title="Visualizza Carrello">
                <span class="cart-icon">🛒</span>
                <span class="cart-label">Carrello</span>
                <span id="cart-badge" class="cart-badge">
                    ${not empty sessionScope.carrello ? sessionScope.carrello.numeroProdotti : 0}
                </span>
            </a>

            <!-- Menu Autenticazione / Profilo -->
            <div class="user-menu">
                <c:choose>
                    <c:when test="${not empty sessionScope.loggedUser}">
                        <div class="user-dropdown">
                            <button class="user-btn" type="button">
                                👤 <span>${sessionScope.loggedUser.nome}</span> ▾
                            </button>
                            <div class="dropdown-menu">
                                <c:if test="${sessionScope.loggedUser.admin}">
                                    <a href="${pageContext.request.contextPath}/admin/dashboard" class="dropdown-item admin-link">
                                        ⚙️ Pannello Admin
                                    </a>
                                </c:if>
                                <a href="${pageContext.request.contextPath}/ordini" class="dropdown-item">
                                    📦 I Miei Ordini
                                </a>
                                <div class="dropdown-divider"></div>
                                <a href="${pageContext.request.contextPath}/logout" class="dropdown-item logout-link">
                                    🚪 Logout
                                </a>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login" class="login-btn">
                            👤 Accedi / Registrati
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Hamburger Button per Menu Mobile -->
            <button class="mobile-menu-toggle" id="mobile-menu-toggle" aria-label="Menu di navigazione">
                ☰
            </button>
        </div>
    </div>

    <!-- Barra di Navigazione Categorie -->
    <nav class="main-navbar" id="main-navbar">
        <div class="nav-container">
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo">Tutti i Prodotti</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo?categoria=1">Sensori CGM</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo?categoria=2">Glucometri</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo?categoria=3">Strisce Reattive</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo?categoria=4">Pungidito e Aghi</a></li>
                <li><a href="${pageContext.request.contextPath}/catalogo?categoria=5">Accessori</a></li>
            </ul>
        </div>
    </nav>
</header>
