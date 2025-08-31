package Framework.resources;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;

public class ExtendReporterNG {

    public static ExtentReports getReportObject() {
        // Use project root and relative path
        String projectPath = System.getProperty("user.dir");
        String reportDir = projectPath + File.separator + "reports";
        File dir = new File(reportDir);
        if (!dir.exists()) {
            dir.mkdirs();  // create reports folder if it doesn't exist
        }

        // Optional: add timestamp to avoid overwriting previous reports
        String reportPath = reportDir + File.separator + "index.html";

        System.out.println("Report path: " + reportPath);
        ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
        reporter.config().setReportName("Web Automation Results");
        reporter.config().setDocumentTitle("Test Results");

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Tester", "Adhish Singh");

        return extent;
    }
}