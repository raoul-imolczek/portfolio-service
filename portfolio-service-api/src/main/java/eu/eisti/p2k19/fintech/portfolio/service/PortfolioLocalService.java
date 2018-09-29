package eu.eisti.p2k19.fintech.portfolio.service;

import java.util.List;
import java.util.Map;

import eu.eisti.p2k19.fintech.portfolio.exceptions.IncorrectProfitabilityFileException;
import eu.eisti.p2k19.fintech.portfolio.exceptions.IncorrectQuotationsFileException;

public interface PortfolioLocalService {

	public static final String VARIANCE = "VARIANCE";
	
	public Map<String, Double> getOptimalVarianceWeights(List<String> symbols, double expectedPortfolioProfitability) throws IncorrectQuotationsFileException, IncorrectProfitabilityFileException;
	
	public List<String> getSymbols() throws IncorrectQuotationsFileException, IncorrectProfitabilityFileException;
	
}
