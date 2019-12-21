package com.fantasticfour.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fantasticfour.entity.CurrencyConversionBean;
import com.fantasticfour.feignclient.CurrencyExchangeProxy;

@RestController
public class CurrencyConversionController {

	@Autowired
	CurrencyExchangeProxy currencyExchangeProxy;
	
	private Logger longer = LoggerFactory.getLogger(this.getClass());
	
	//Using Feign Client
	@GetMapping("currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrencyfeign(@PathVariable String from,
			@PathVariable String to,@PathVariable BigDecimal quantity) {
		
		CurrencyConversionBean response = currencyExchangeProxy.getExchangeValue(from, to);
		
		longer.info("{}",response);
		
		return new CurrencyConversionBean(response.getId(),from,to,
				response.getConversionMultiple(),
				quantity,quantity.multiply(response.getConversionMultiple()),
				response.getPort());
	}
	
	//Using Rest Client
	@GetMapping("currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrency(@PathVariable String from,
			@PathVariable String to,@PathVariable BigDecimal quantity
			) {
		Map<String,String> uriVariables=new HashMap();
		uriVariables.put("from", from);
		uriVariables.put("to",to);
		
		
		ResponseEntity<CurrencyConversionBean> responseEntity=new RestTemplate().getForEntity
				("http://localhost:8000/currency-exchange/from/{from}/to/{to}", 
				CurrencyConversionBean.class, uriVariables);
		
		CurrencyConversionBean response = responseEntity.getBody();
		
		return new CurrencyConversionBean(response.getId(),from,to,
				response.getConversionMultiple(),
				quantity,quantity.multiply(response.getConversionMultiple()),
				response.getPort());
	}
	
	
}
