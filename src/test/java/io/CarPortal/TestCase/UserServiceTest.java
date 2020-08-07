package io.CarPortal.TestCase;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.CarPortal.Model.User;
import io.CarPortal.Model.UserDTO;
import io.CarPortal.Utilities.CommonUtilities;
import io.CarPortal.Utilities.WebServiceUtilities;
import io.restassured.response.Response;

public class UserServiceTest extends BaseTest {

	private final static Logger logger = LoggerFactory.getLogger(UserServiceTest.class);
	private final ObjectMapper mapper = new ObjectMapper();
	private final String userHostURL = getProps().getProperty("USER_HOST_URL");
	private final String userServiceURLGetAllInfo = getProps().getProperty("USER_SERVICE_URL_GET_ALL_INFO");
	private final String userServiceURLCreateNewUser = getProps().getProperty("USER_SERVICE_URL_CREATE_NEW_USER");
	private final Map<String,String> headers=new HashMap<>();
	

	@BeforeTest
	public void setUp() {
		mapper.registerModule(new JavaTimeModule());
		headers.put("Content-Type", "application/json");
	}
	
	@DataProvider
	public Object[][] getUserInformation(){
		return new Object[][] {
			{CommonUtilities.getRandomData.get(),"TestUser-"+CommonUtilities.getRandomData.get()},
				{CommonUtilities.getRandomData.get(),"TestUser-"+CommonUtilities.getRandomData.get()}
		};
	}
	
	@Test(priority = 1, description = "Verify New User Creation", dataProvider = "getUserInformation")
	public void tcVerifyNewUserServiceTest(Integer userid,String username) {
		try {
			logger.info(Thread.currentThread().getStackTrace()[2].getMethodName()+" has started its Execution");
			String jsonPayloadString="";
			User userRequestObj=new User(userid,username);
				try {
						jsonPayloadString=mapper.writeValueAsString(userRequestObj);
					}catch(JsonProcessingException e) {
						String exceptionMsg=CommonUtilities.stracktraceToString.apply(e);
						logger.error("Failed in JSON Serializing. Exception Details:"+exceptionMsg);
						Assert.fail("Failed in JSON Serializing. Exception Details:"+exceptionMsg);
			}
			Response responseObj=WebServiceUtilities.postRequest(userHostURL+userServiceURLCreateNewUser, headers, jsonPayloadString);
			Assert.assertTrue(CommonUtilities.verifyStatusCode.test(responseObj.getStatusCode()),
			                    "Expected HttpResponse of CREATED or SUCCESS or OK is not received. Please review your request. Actual Response Code received:"+responseObj.getStatusCode());
		}finally {
			logger.info(Thread.currentThread().getStackTrace()[2].getMethodName()+" has end its Execution");
		}
	}
	
	@Test(priority = 2, description="Verify Search of All Users Available", dependsOnMethods = "tcVerifyNewUserServiceTest")
	public void tcVerifySearchAllUsersInformationTest() {
		try {
			UserDTO userWrapperResponseObj=null;
			logger.info(Thread.currentThread().getStackTrace()[2].getMethodName()+" has started its Execution");
			Response responseObj=WebServiceUtilities.getRequest(userHostURL+userServiceURLGetAllInfo, headers);
			try {
					userWrapperResponseObj=mapper.readValue(responseObj.asString(), UserDTO.class);
			}catch(JsonProcessingException e) {
				String exceptionMsg=CommonUtilities.stracktraceToString.apply(e);
				logger.error("Failed in JSON De-Serializing. Exception Details:"+exceptionMsg);
				Assert.fail("Failed in JSON De-Serializing. Exception Details:"+exceptionMsg);
			}
			Assert.assertTrue(CommonUtilities.greaterThan.apply(userWrapperResponseObj.getUsers().size(),0),"Please review your test data.");
		}finally {
			logger.info(Thread.currentThread().getStackTrace()[2].getMethodName()+" has end its Execution");
		}
	}

}
