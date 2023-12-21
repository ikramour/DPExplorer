<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Mon DP Explorer</title>
</head>
<body>

	<jai:deception>
	Si jamais cette balise passe sur la page HTML, je suis très déçu !
	</jai:deception>
    <fmt:setBundle basename="MonApp"/>
    ${request.header[accept-language]}
	<h1>Ma cible</h1>
	<c:if test="${erreurDeFormulaire}">
		<p><fmt:message key="erreur.global"/></p>
	</c:if>
	<form action="search">
		<c:if test="${erreurDeFormulaireCP}">
			<p><fmt:message key="erreur.cp"/></p>
		</c:if>
		<input type="text" name="CP" size="5" placeholder="CP"
			value="${param.CP}" />
		<c:if test="${erreurDeFormulaireVille}">
			<p><fmt:message key="erreur.ville"/></p>
		</c:if>
		<input type="text" name="Ville" size="60" placeholder="Ville"
			value="${param.Ville}">
		<c:if test="${erreurDeFormulaireRue}">
			<p><fmt:message key="erreur.rue"/></p>
		</c:if>
		<input type="text" name="Rue" size="80" placeholder="Rue"
			value="${param.Rue}" /> <input type="submit" />
	</form>
</body>
</html>