<%@ include file="/init.jsp" %>

<h1>Minimal variance portfolio</h1>

<ul>
	<c:forEach items="${symbols}" var="symbol">
		<li>${symbol}</li>
	</c:forEach> 
</ul>
