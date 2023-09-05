
package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


public class ConfigReader {
	

	
	private ConfigReader(){
		
	}
	public static Properties loadPropertyFile(String filePath) {
		File file = new File(filePath);
		Properties prop = new Properties();

		FileInputStream fileInput = null;
		try {
			fileInput = new FileInputStream(file);
			prop.load(fileInput);
		} catch (Exception e) {
			LogUtil.errorLog(ConfigReader.class, "Caught the exception", e);
		}
		return prop;

	}
	
	public static String getValue(String key) {

		Properties prop = loadPropertyFile("src/main/resources/Config/config.properties");
		
		 return prop.getProperty(key);
	}
	
	public static int getIntValue(String key) {
		Properties prop = loadPropertyFile("src/main/resources/Config/config.properties");

		
		String strKey = prop.getProperty(key);

		return Integer.parseInt(strKey);
	}

}
