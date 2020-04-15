package abs.framework.logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportClass {
	public ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public static ExtentTest logger;
	public ExtentReportClass(String TestTitle) {

		//Reporter Code
		htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-output/ABSExtentReport.html");
		htmlReporter.config().enableTimeline(false);
		htmlReporter.config().setDocumentTitle("AUTOMATION");
		htmlReporter.config().setReportName("ABS FREEDOM SANITY AUTOMATION REPORT");
		htmlReporter.config().setTheme(Theme.DARK);
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		logger = extent.createTest(TestTitle);
	
	}

	public static ExtentTest initiateLogger() {
		return logger;

	}

}
