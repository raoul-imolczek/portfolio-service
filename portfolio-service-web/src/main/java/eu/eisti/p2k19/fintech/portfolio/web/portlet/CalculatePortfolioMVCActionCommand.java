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
public class CalculatePortfolioMVCActionCommand implements MVCActionCommand {

	@Override
	public boolean processAction(ActionRequest actionRequest, ActionResponse actionResponse) throws PortletException {

		Map<String, String[]> map = actionRequest.getParameterMap();
		
		double expectedPortfolioProfitability = ParamUtil.getDouble(actionRequest, "PROFIT", 100d) / 100d;
		
		List<String> symbols = new ArrayList<String>();
		
		Iterator<String> fieldIterator = map.keySet().iterator();
		while(fieldIterator.hasNext()) {
			String field = fieldIterator.next();
			System.out.println("FIELD: " + field);
			if(field.startsWith("SYMBOL_") && ParamUtil.getBoolean(actionRequest, field, false)) {
				String symbol = field.substring(7);
				symbols.add(symbol);
			}
		}

		System.out.println("ACTION: " + symbols.size());
		
		try {
			portfolioLocalService.getOptimalVarianceWeights(symbols, expectedPortfolioProfitability);
		} catch (IncorrectQuotationsFileException | IncorrectProfitabilityFileException e) {
			throw new PortletException();
		}
		
		actionResponse.setRenderParameter("mvcPath", "portfolio.jsp");
		
		return false;
	}
	
	@Reference
	private PortfolioLocalService portfolioLocalService;

}
