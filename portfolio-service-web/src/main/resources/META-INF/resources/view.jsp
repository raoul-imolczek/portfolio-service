<%@ include file="/init.jsp" %>

<h1>Select your symbols</h1>

<aui:form action="${selectSymbolsActionURL}" method="POST">
	<aui:fieldset-group markupView="lexicon">
		<aui:fieldset>
			<c:forEach items="${symbols}" var="symbol">
				<aui:input label="${symbol}" type="checkbox" id="SYMBOL_${symbol}" name="SYMBOL_${symbol}" />
			</c:forEach> 
			<aui:input type="number" value="100" id="PROFIT" name="PROFIT" suffix="%" />
		</aui:fieldset>
	</aui:fieldset-group>
	<aui:button-row>
		<aui:button type="submit" />
	</aui:button-row>	
</aui:form>

<c:if test="${not empty portfolio}">
	<h1>Minimal variance portfolio</h1>
	
	<dl>
		<dt>Expected profitability:</dt>
		<dd>${expectedPortfolioProfitability}</dd>
		<dt>Variance:</dt>
		<dd>${variance}</dd>
	</dl>
	
	<ul>
		<c:forEach items="${portfolio}" var="entry">
			<li><strong>${entry.key}:</strong> ${entry.value}</li>
		</c:forEach>
	</ul>	
</c:if>

