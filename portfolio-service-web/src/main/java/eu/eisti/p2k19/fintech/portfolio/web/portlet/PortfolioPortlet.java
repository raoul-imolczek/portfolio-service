package eu.eisti.p2k19.fintech.portfolio.web.portlet;

import java.io.IOException;
import java.util.ArrayList;

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
		
		try {
			renderRequest.setAttribute("symbols", portfolioLocalService.getSymbols());
		} catch (IncorrectQuotationsFileException | IncorrectProfitabilityFileException e) {
			renderRequest.setAttribute("symbols", new ArrayList<String>());
		}
		
		super.doView(renderRequest, renderResponse);
	}
	
	@Reference
	private PortfolioLocalService portfolioLocalService;	
	
}
