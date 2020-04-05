package abs.framework.logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenShots {

	public static WebDriver driver;

	public ScreenShots(WebDriver driver) {
		ScreenShots.driver = driver;
	}

	public static String getScreenShot() throws IOException {

		try {

			String dateName = new SimpleDateFormat("ddMMyyyyhhmmss").format(new Date());
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			String destination = "C:\\temp\\" + Paths.get(System.getProperty("user.dir")).getFileName() + "_" + dateName
					+ ".png";
			File finalDestination = new File(destination);
			FileUtils.copyFile(source, finalDestination);
			return destination;

		} catch (Exception e) {
			throw e;
		}

	}

}
