package abs.framework.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Properties;

import org.testng.Reporter;

import com.mongodb.diagnostics.logging.Logger;

public class Util {
	public static Properties prop;
	public static Connection con;
	public static Statement stmt;

	
	
	/**
	 * Checks if is element present.
	 *
	 * @name GetPropertyObject
	 * @description get the priorty object
	 * @author Vaibhav Narkhede
	 * @param NA ||description: NA ||allowedRange:
	 * @return Property object
	 * @jiraId
	 */
	public Properties GetPropertyObject() {

		return prop;

	}
	
	
	
	
	/**
	 * Checks if is element present.
	 *
	 * @name CreatePropertyfile
	 * @description create the properties file
	 * @author Vaibhav Narkhede
	 * @param NA ||description: NA ||allowedRange:
	 * @return NA
	 * @jiraId
	 */
	public void CreatePropertyfile() {
		File file = new File(System.getProperty("user.dir") + "\\configuration\\config.properties");

		FileInputStream fileInput = null;

		try {

			fileInput = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		prop = new Properties();

		// load properties file
		try {
			prop.load(fileInput);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Decrypt the string 
	 *
	 * @name decryptString
	 * @description This method will decrypt the string pass to this function
	 * @author Vaibhav Narkhede
	 * @param text ||description: String to be decrypted ||allowedRange: 
	 * @return NA
	 * @jiraId
	 */
	public static String decryptString(String text) {

		try {

			byte[] decodedBytes = Base64.getDecoder().decode(text.getBytes());

			return new String(decodedBytes);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * create oracle database connection
	 *
	 * @name CreateConnection
	 * @description This method will create the connection with oracle Database
	 * @author Vaibhav Narkhede
	 * @param NA ||description: ||allowedRange: 
	 * @return NA
	 * @jiraId
	 */
	public void CreateConnection() throws Exception {
		try {
	
			Class.forName("oracle.jdbc.driver.OracleDriver");
		// con=DriverManager.getConnection("jdbc:oracle:thin:@freddev9.oracle.eagle.org:1521:freddev9","VNARKHEDE","freddev92020");
		//	con = DriverManager.getConnection(
				//	"jdbc:oracle:thin:@" + prop.getProperty("env.environment") + ".oracle.eagle.org:1521:"
					//		+ prop.getProperty("env.dbusername"),
				//	prop.getProperty("env.environment"), Util.decryptString(prop.getProperty("env.dbpassword")));
		 
		 con = DriverManager.getConnection(
					"jdbc:oracle:thin:@"+prop.getProperty("env.environment") + ".oracle.eagle.org:1521:"+prop.getProperty("env.environment"),
							 prop.getProperty("env.dbusername"),
					Util.decryptString(prop.getProperty("env.dbpassword"))); 
		 
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		} catch (Exception e)

		{

			throw e;

		}

	}

	
	/**
	 * get the database object
	 *
	 * @name getDBObject
	 * @description This method will return the database object
	 * @author Vaibhav Narkhede
	 * @param NA ||description: ||allowedRange: 
	 * @return NA
	 * @jiraId
	 */
	public Statement getDBObject() {

		return stmt;

	}

}
