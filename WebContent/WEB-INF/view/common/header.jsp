<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="DiaTech Solutions - E-commerce specializzato in dispositivi medici e accessori per il monitoraggio del diabete.">
    <title>${not empty pageTitle ? pageTitle : 'DiaTech Solutions | Dispositivi Medici per il Diabete'}</title>

    <!-- Fogli di Stile CSS Esterni (risorse statiche conformi alle direttive) -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/responsive.css">
    <c:if test="${not empty extraCss}">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/${extraCss}">
    </c:if>

    <!-- Icona Favicon -->
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/favicon.png">
</head>
<body>
