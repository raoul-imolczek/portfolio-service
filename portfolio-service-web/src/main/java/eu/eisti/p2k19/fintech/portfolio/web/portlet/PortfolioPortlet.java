package eu.eisti.p2k19.fintech.portfolio.web.portlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import eu.eisti.p2k19.fintech.portfolio.exceptions.IncorrectProfitabilityFileException;
import eu.eisti.p2k19.fintech.portfolio.exceptions.IncorrectQuotationsFileException;
import eu.eisti.p2k19.fintech.portfolio.service.PortfolioLocalService;

@Component (
		immediate = true,
		property = {
				"com.liferay.portlet.css-class-wrapper=portlet-jsp",
				"com.liferay.portlet.display-category=category.sample",
				"com.liferay.portlet.header-portlet-css=/css/main.css",
				"com.liferay.portlet.instanceable=true",
				"javax.portlet.display-name=Portfolio Portlet",
				"javax.portlet.init-param.template-path=/",
				"javax.portlet.init-param.view-template=/view.jsp",
				"javax.portlet.name=portfolio",
				"javax.portlet.resource-bundle=content.Language",
				"javax.portlet.security-role-ref=power-user,user"
			},		
		service = Portlet.class
		)
public class PortfolioPortlet extends MVCPortlet {
	
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {

		PortletURL selectSymbolsActionURL = renderResponse.createActionURL();
		selectSymbolsActionURL.setParameter("javax.portlet.action", "/portfolio/calculate");
		
		renderRequest.setAttribute("selectSymbolsActionURL", selectSymbolsActionURL.toString());
		
		List<Map<String, Double>> portfolios; 
		portfolios = (List<Map<String, Double>>) renderRequest.getPortletSession().getAttribute("portfolios");
		if (portfolios != null && renderRequest.getParameter("portfolioId") != null) {
			Map<String, Double> portfolio = portfolios.get(Integer.parseInt(renderRequest.getParameter("portfolioId")));
			loadPortfolio(portfolio, renderRequest);
		}
		
		try {
			renderRequest.setAttribute("symbols", portfolioLocalService.getSymbols());
		} catch (IncorrectQuotationsFileException | IncorrectProfitabilityFileException e) {
			renderRequest.setAttribute("symbols", new ArrayList<String>());
		}
		
		super.doView(renderRequest, renderResponse);
	}
	
	@Reference
	private PortfolioLocalService portfolioLocalService;
	
	private void loadPortfolio(Map<String, Double> portfolioData, RenderRequest renderRequest) {
		Map<String, Double> portfolio = new HashMap<String, Double>();
		Iterator<String> symbols = portfolioData.keySet().iterator();
		while(symbols.hasNext()) {
			String symbol = symbols.next();
			if (PortfolioLocalService.VARIANCE.equals(symbol)) {
				renderRequest.setAttribute("variance", portfolioData.get(symbol));
			} else {
				portfolio.put(symbol, portfolioData.get(symbol));
			}
		}
		renderRequest.setAttribute("expectedPortfolioProfitability", renderRequest.getParameter("expectedPortfolioProfitability"));
		renderRequest.setAttribute("portfolio", portfolio);
	}
	
}
