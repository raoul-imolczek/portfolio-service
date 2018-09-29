package eu.eisti.p2k19.fintech.portfolio.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.linear.RealVector;
import org.osgi.service.component.annotations.Component;

import eu.eisti.p2k19.fintech.portfolio.exceptions.IncorrectProfitabilityFileException;
import eu.eisti.p2k19.fintech.portfolio.exceptions.IncorrectQuotationsFileException;
import eu.eisti.p2k19.fintech.portfolio.model.Profitability;
import eu.eisti.p2k19.fintech.portfolio.model.Quotation;
import eu.eisti.p2k19.fintech.portfolio.optimizer.PortfolioOptimizer;
import eu.eisti.p2k19.fintech.portfolio.service.PortfolioLocalService;
import eu.eisti.p2k19.fintech.portfolio.util.FileReaderUtil;

@Component (
		immediate = true,
		service = PortfolioLocalService.class
		)
public class PortfolioLocalServiceImpl implements PortfolioLocalService {

	@Override
	public Map<String, Double> getOptimalVarianceWeights(List<String> symbols, double expectedPortfolioProfitability) throws IncorrectQuotationsFileException, IncorrectProfitabilityFileException {

		FileReaderUtil util = new FileReaderUtil();

		Map<String, List<Quotation>> quotationsMap = new HashMap<String, List<Quotation>>();

		Iterator<String> symbolsIterator = symbols.iterator();
		Map<String, Double> expectedProfitabilityMap = new HashMap<String, Double>();

		while(symbolsIterator.hasNext()) {
			String symbol = symbolsIterator.next();
			quotationsMap.put(symbol, util.readValues(symbol));
			expectedProfitabilityMap.put(symbol, util.readEstimatedProfitability(symbol));
		}

		PortfolioOptimizer optimizer = new PortfolioOptimizer(quotationsMap, expectedProfitabilityMap, expectedPortfolioProfitability);
		
		RealVector solution = optimizer.computeSolution();
		double variance = optimizer.getVariance();
		
		optimizer.printExpectedProfitabilities();
		
		Map<String, Double> result = new HashMap<String, Double>();
		
		symbolsIterator = symbols.iterator();
		int k = 0;
		while(symbolsIterator.hasNext()) {
			String symbol = symbolsIterator.next();
			result.put(symbol, solution.getEntry(k));
			k++;
		}
		result.put(PortfolioLocalService.VARIANCE, variance);
		
		return result;
	}

	@Override
	public List<String> getSymbols() throws IncorrectQuotationsFileException, IncorrectProfitabilityFileException {

		FileReaderUtil util = new FileReaderUtil();

		return util.readSymbols();
	}

}
