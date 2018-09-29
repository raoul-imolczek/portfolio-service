<%@ include file="/init.jsp" %>

<h1>Select your symbols</h1>

<form action="${selectSymbolsActionURL}" method="POST">
	<c:forEach items="${symbols}" var="symbol">
		<label for="${symbol}">${symbol}</label><input type="checkbox" id="${symbol}" name="${symbol}" />
	</c:forEach> 
</form>
