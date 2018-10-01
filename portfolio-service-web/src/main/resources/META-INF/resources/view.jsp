<%@ include file="/init.jsp" %>

<h1>Select your symbols</h1>

<aui:form action="${selectSymbolsActionURL}" method="POST">
	<c:forEach items="${symbols}" var="symbol">
		<aui:input label="${symbol}" type="checkbox" id="SYMBOL_${symbol}" name="SYMBOL_${symbol}" /><br />
	</c:forEach> 
	<aui:input type="number" value="100" id="PROFIT" name="PROFIT" /> %<br />
	<aui:input type="submit" value="Submit" name="SUBMIT" />
</aui:form>
