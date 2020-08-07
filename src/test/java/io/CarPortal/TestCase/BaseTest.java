package io.CarPortal.TestCase;

import java.io.FileInputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.CarPortal.Utilities.CommonUtilities;

public class BaseTest {

	private static Logger logger=LoggerFactory.getLogger(BaseTest.class);
	private Properties props=null;

	public Properties getProps() {
		return props;
	}

	public BaseTest() {
		logger.info(Thread.currentThread().getStackTrace()[2].getMethodName()+" has started its Execution");
		try {
				props=new Properties();
				FileInputStream fis=new FileInputStream(System.getProperty("user.dir")+"/src/test/java/io/CarPortal/Config/config.properties");
				props.load(fis);
		}catch(Exception e) {
			logger.error("Failed in loading Property File. Exception Details:"+CommonUtilities.stracktraceToString.apply(e));
			throw new RuntimeException("Failed in loading Property File. Exception Details:"+e.getMessage());
		}finally {
			logger.info(Thread.currentThread().getStackTrace()[2].getMethodName()+" has ended its Execution");
		}
	}
	

}
