package uk.axone.extentreports;

import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.util.Date;

public class ExtentReportsManager {

    private static ExtentReports report;

    public synchronized static ExtentReports getExtentReports() {
        if(report == null ){
            Date d = new Date();
            String fileName = d.toString().replace(" ","-").replace(":","_")+".html";

            //1. to create an instance of Extent Reports.
            report = new ExtentReports(SystemUtils.USER_DIR+"/target/reports/"+fileName,true, DisplayOrder.OLDEST_FIRST);

            //2. to load the config file.
            report.loadConfig(new File(SystemUtils.USER_DIR+"/src/test/resources/config.xml"));

        }
        return report;
    }
}
