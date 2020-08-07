package io.CarPortal.TestCase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.CarPortal.Model.*;
import io.CarPortal.Utilities.CommonUtilities;
import io.CarPortal.Utilities.WebServiceUtilities;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingServiceTest extends BaseTest{

    private final static Logger logger = LoggerFactory.getLogger(UserServiceTest.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final String carHostURL = getProps().getProperty("CAR_HOST_URL");
    private final String carServiceURLGetAllInfo = getProps().getProperty("CAR_SERVICE_URL_GET_ALL_INFO");
    private final String userHostURL = getProps().getProperty("USER_HOST_URL");
    private final String userServiceURLGetAllInfo = getProps().getProperty("USER_SERVICE_URL_GET_ALL_INFO");
    private final String bookingHostURL = getProps().getProperty("BOOKING_HOST_URL");
    private final String bookingServiceURLGetAllInfo = getProps().getProperty("BOOKING_SERVICE_URL_GET_ALL_INFO");
    private final String bookingServiceURLCreateNewBooking = getProps().getProperty("BOOKING_SERVICE_URL_CREATE_NEW_CAR");
    private UserDTO userWrapperResponseObj = null;
    private CarDTO carWrapperResponseObj = null;
    private CarBookingDTO bookingWrapperResponseObj = null;
    private final Map<String,String> headers=new HashMap<>();

    @BeforeTest
    public void setUp() {
        mapper.registerModule(new JavaTimeModule());
        headers.put("Content-Type", "application/json");
    }

    @Test(priority = 1, description = "Verify creation of New Car Booking.")
    public void fncVerifyNewCarBooking()  {
        try {
            logger.info(Thread.currentThread().getStackTrace()[2].getMethodName() + " has started to execute.");
            String jsonBookingPayload = "";
            headers.put("Content-Type", "application/json");
            Response userResponseObj = WebServiceUtilities.getRequest(userHostURL + userServiceURLGetAllInfo, headers);
            try {
                userWrapperResponseObj = mapper.readValue(userResponseObj.asString(), UserDTO.class);
            } catch (JsonProcessingException e) {
                logger.error("Exception in De-Serializing JSON String to User Object. Exception Details:"+e.getMessage());
                Assert.fail("Exception in De-Serializing JSON String to User Object. Exception Details:"+ CommonUtilities.stracktraceToString.apply(e));
            }
            List<User> listOfUsersAvailable = userWrapperResponseObj.getUsers();
            if (!(listOfUsersAvailable.size() > 0)) {
                logger.error("No User Information is returned. Please review the User Microservice and data.");
                Assert.fail("No User Information is returned. Please review the User Microservice and data.");
            }
            Integer useridInput = listOfUsersAvailable.get(CommonUtilities.get_RandomIndexNumber_FromList(listOfUsersAvailable)).getUserid();
            Response carResponseObj = WebServiceUtilities.getRequest(carHostURL + carServiceURLGetAllInfo, headers);
            try {
                carWrapperResponseObj = mapper.readValue(carResponseObj.asString(), CarDTO.class);
            } catch (JsonProcessingException e) {
                logger.error("Exception in De-Serializing JSON String to Car Object. Exception Details:"+e.getMessage());
                Assert.fail("Exception in De-Serializing JSON String to Car Object. Exception Details:"+ CommonUtilities.stracktraceToString.apply(e));
            }
            List<Car> listOfCarsAvailable = carWrapperResponseObj.getCars();
            if (!(listOfCarsAvailable.size() > 0)) {
                logger.error("No Car Information is returned. Please review the Car Microservice and data.");
                Assert.fail("No Car Information is returned. Please review the Car Microservice and data.");
            }
            Car carObj = listOfCarsAvailable.get(CommonUtilities.get_RandomIndexNumber_FromList(listOfCarsAvailable));
            Integer carPlateNumber = carObj.getCarPlateNumber();
            LocalDateTime requestedStartBookingDate = CommonUtilities.getRandomLocalDateTimeShortRange
                    .apply(carObj.getCarAvailableStartDate());
            LocalDateTime requestedEndBookingDate = requestedStartBookingDate.plusDays(5);
            CarBooking carBookings = new CarBooking();
            carBookings.setCarPlateNumber(carPlateNumber);
            carBookings.setUserId(useridInput);
            carBookings.setBookingStartDate(requestedStartBookingDate);
            carBookings.setBookingEndDate(requestedEndBookingDate);
            carBookings.setBookingTime(LocalDateTime.now());
            try {
                jsonBookingPayload = mapper.writeValueAsString(carBookings);
            } catch (JsonProcessingException e) {
                logger.error("Exception in Serializing Car Object to JSON String. Exception Details:"+e.getMessage());
                Assert.fail("Exception in Serializing Car Object to JSON String. Exception Details:"+ CommonUtilities.stracktraceToString.apply(e));
            }
            Response bookingResponse = WebServiceUtilities.postRequest(bookingHostURL + bookingServiceURLCreateNewBooking, headers, jsonBookingPayload);
            int statusCode=bookingResponse.getStatusCode();
            Assert.assertTrue(CommonUtilities.verifyStatusCode.test(statusCode),
                    "Expected HttpResponse of CREATED or SUCCESS or OK is not received. Please review your request. Actual Response Code received:"+statusCode);
        } finally {
            logger.info(Thread.currentThread().getStackTrace()[2].getMethodName() + " has completed its execution.");
        }
    }

    @Test(priority = 2, description = "Verify existing Bookings to be searched.", dependsOnMethods = "fncVerifyNewCarBooking")
    public void fncVerifySearchExistingBookings(){
        try{
            logger.info(Thread.currentThread().getStackTrace()[2].getMethodName() + " has started to execute.");
            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Content-Type","application/json");
            Response bookingResponseObj = WebServiceUtilities.getRequest(bookingHostURL + bookingServiceURLGetAllInfo, headers);
            int statusCode=bookingResponseObj.getStatusCode();
            Assert.assertTrue(CommonUtilities.verifyStatusCode.test(statusCode),
                    "Expected HttpResponse of SUCCESS or OK is not received. Please review your request. Actual Response Code received:"+statusCode);
            try {
                bookingWrapperResponseObj = mapper.readValue(bookingResponseObj.asString(), CarBookingDTO.class);
            } catch (JsonProcessingException e) {
                logger.error("Exception in De-Serializing JSON String to User Object. Exception Details:"+e.getMessage());
                Assert.fail("Exception in De-Serializing JSON String to User Object. Exception Details:"+ CommonUtilities.stracktraceToString.apply(e));
            }
            List<CarBooking> listOfBookings=bookingWrapperResponseObj.getBookings();
            if (!(listOfBookings.size() > 0)) {
                logger.error("No Booking Information is returned. Please review the Booking Microservice and data.");
                Assert.fail("No Booking Information is returned. Please review the Booking Microservice and data.");
            }
        }finally {
            logger.info(Thread.currentThread().getStackTrace()[2].getMethodName() + " has completed its execution.");
        }
    }
}
