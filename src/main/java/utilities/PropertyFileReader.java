package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Properties;

import Constants.FrameworkConstants;

/*******************************************************************************************
 * Property Utility - methods to read & write in property files
 * @author anjana.iv
 *******************************************************************************************/
public class PropertyFileReader {

	private PropertyFileReader() {
		initProperties();
	}

	private static Properties property = new Properties();

	public static String get(String key) throws Exception {
		initProperties();
		if (Objects.isNull(property.getProperty(key)) || Objects.isNull(key)){
			throw new Exception("Property name "+key + " is not found in properties file");
		}
		return property.getProperty(key);
	}

	public static void initProperties() {
		try {
			final File file = new File(FrameworkConstants.getPropertyfilepath());
			if (file == null) {
				throw new FileNotFoundException();
			}
			final FileInputStream fileInput = new FileInputStream(file);
			property.load(fileInput);
			fileInput.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}

