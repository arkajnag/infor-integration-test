package io.CarPortal.Utilities;

import java.util.Map;
import org.slf4j.*;
import org.slf4j.LoggerFactory;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;


public interface WebServiceUtilities {
	
	public static Logger logger=LoggerFactory.getLogger(WebServiceUtilities.class);
	
	public static Response getRequest(String baseURI, Map<String,String> headers) {
		logger.info(Thread.currentThread().getStackTrace()[1].getMethodName()+" has started");
		try{
			return RestAssured.given()
			.headers(headers)
			.request(Method.GET, baseURI);
		}catch(Exception e) {
			String exceptionAsString = CommonUtilities.stracktraceToString.apply(e);
			logger.error("Exception:"+e.getMessage()+" and its Details:"+exceptionAsString);
			throw new RuntimeException("Exception:"+e.getMessage()+" and its Details:"+exceptionAsString);
		}finally {
			logger.info(Thread.currentThread().getStackTrace()[1].getMethodName()+" has ended");
		}
	}
	
	public static Response postRequest(String baseURI,Map<String,String> headers,String jsonStringPayload) {
		logger.info(Thread.currentThread().getStackTrace()[1].getMethodName()+" has started");
		try {
			return RestAssured.given()
					.headers(headers)
					.body(jsonStringPayload)
					.request(Method.POST, baseURI);
		}catch(Exception e) {
			String exceptionAsString = CommonUtilities.stracktraceToString.apply(e);
			logger.error("Exception:"+e.getMessage()+" and its Details:"+exceptionAsString);
			throw new RuntimeException("Exception:"+e.getMessage()+" and its Details:"+exceptionAsString);
		}finally {
			logger.info(Thread.currentThread().getStackTrace()[1].getMethodName()+" has ended");
		}
	}

}
