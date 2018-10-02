package eu.eisti.p2k19.fintech.portfolio.web.portlet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.util.ParamUtil;

import eu.eisti.p2k19.fintech.portfolio.exceptions.IncorrectProfitabilityFileException;
import eu.eisti.p2k19.fintech.portfolio.exceptions.IncorrectQuotationsFileException;
import eu.eisti.p2k19.fintech.portfolio.service.PortfolioLocalService;

@Component(
		immediate = true,
	    property = {
				"javax.portlet.name=portfolio",
				"mvc.command.name=/portfolio/calculate"
	    	    },		
		service = MVCActionCommand.class
		)
public class CalculatePortfolioMVCActionCommand extends BaseMVCActionCommand {

	@Override
	public void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException {

		Map<String, String[]> map = actionRequest.getParameterMap();
		
		double expectedPortfolioProfitability = ParamUtil.getDouble(actionRequest, "PROFIT", 100d) / 100d;
		
		List<String> symbols = new ArrayList<String>();
		
		Iterator<String> fieldIterator = map.keySet().iterator();
		while(fieldIterator.hasNext()) {
			String field = fieldIterator.next();
			if(field.startsWith("SYMBOL_") && ParamUtil.getBoolean(actionRequest, field, false)) {
				String symbol = field.substring(7);
				symbols.add(symbol);
			}
		}
		
		try {
			Map<String, Double> optimalPortfolio = portfolioLocalService.getOptimalVarianceWeights(symbols, expectedPortfolioProfitability);
			
			List<Map<String, Double>> portfolios; 
			portfolios = (List<Map<String, Double>>) actionRequest.getPortletSession().getAttribute("portfolios");
			if (portfolios == null) {
				portfolios = new ArrayList<Map<String, Double>> ();
				actionRequest.getPortletSession().setAttribute("portfolios", portfolios);
			}
			portfolios.add(optimalPortfolio);
			actionResponse.setRenderParameter("portfolioId", new StringBuffer().append(portfolios.size() - 1).toString());
			actionResponse.setRenderParameter("expectedPortfolioProfitability", new StringBuffer().append(expectedPortfolioProfitability).toString());
			
		} catch (IncorrectQuotationsFileException | IncorrectProfitabilityFileException e) {
			throw new PortletException();
		}
		
	}
	
	@Reference
	private PortfolioLocalService portfolioLocalService;

}
