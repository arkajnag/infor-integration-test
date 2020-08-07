package io.CarPortal.TestCase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.CarPortal.Model.Car;
import io.CarPortal.Utilities.CommonUtilities;
import io.CarPortal.Utilities.WebServiceUtilities;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

public class CarServiceTest extends BaseTest{

    private final static Logger logger = LoggerFactory.getLogger(UserServiceTest.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final String carHostURL = getProps().getProperty("CAR_HOST_URL");
    private final String carServiceURLCreateNewUser = getProps().getProperty("CAR_SERVICE_URL_CREATE_NEW_CAR");
    private final Map<String,String> headers=new HashMap<>();

    @BeforeTest
    public void setUp() {
        mapper.registerModule(new JavaTimeModule());
        headers.put("Content-Type", "application/json");
    }

    @Test(priority = 1, description = "Verify New Car Details Registered in Car Portal", invocationCount = 3)
    public void tcVerifyCreateNewCarServiceTest(){
        try{
            logger.info(Thread.currentThread().getStackTrace()[2].getMethodName()+" has started its Execution");
            String jsonPayloadString="";
            Car requestCarObj=new Car();
            requestCarObj.setCarPlateNumber(CommonUtilities.getRandomData.get());
            requestCarObj.setCarModelName("Car-Model-"+CommonUtilities.getRandomData.get());
            requestCarObj.setCarAvailableStartDate(CommonUtilities.getLocalDateTimeFromDateString.apply("2020-08-15T15:02:22"));
            requestCarObj.setCarAvailableEndDate(CommonUtilities.getLocalDateTimeFromDateString.apply("2020-08-25T15:02:22"));
            requestCarObj.setCarRentalPricePerHour(CommonUtilities.getRandomDoubleWithinRange.apply(10.0,20.0));
            try {
                    jsonPayloadString=mapper.writeValueAsString(requestCarObj);
            }catch (JsonProcessingException e){
                String exceptionMsg=CommonUtilities.stracktraceToString.apply(e);
                logger.error("Failed in JSON Serializing. Exception Details:"+exceptionMsg);
                Assert.fail("Failed in JSON Serializing. Exception Details:"+exceptionMsg);
            }
            Response responseObj=WebServiceUtilities.postRequest(carHostURL+carServiceURLCreateNewUser,headers,jsonPayloadString);
            Assert.assertTrue(CommonUtilities.verifyStatusCode.test(responseObj.getStatusCode()),
                    "Expected HttpResponse of CREATED or SUCCESS or OK is not received. Please review your request. Actual Response Code received:"+responseObj.getStatusCode());
        }finally {
            logger.info(Thread.currentThread().getStackTrace()[2].getMethodName()+" has end its Execution");
        }
    }
}
