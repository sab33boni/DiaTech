<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Reindirizzamento immediato verso il controller HomeServlet (/home)
    // per rispettare il pattern architetturale MVC ed evitare l'accesso diretto a viste JSP
    response.sendRedirect(request.getContextPath() + "/home");
%>
