import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.quartz.SimpleScheduleBuilder.*;

public class ServletContextClass implements ServletContextListener {

	static Scheduler mScheduler;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Server destroyed");
		try {
			System.out.println("Shutdown scheduler");
			mScheduler = StdSchedulerFactory.getDefaultScheduler();
			mScheduler.shutdown(false);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Server started");
		
		try {
			mScheduler = StdSchedulerFactory.getDefaultScheduler();
			if (mScheduler.isStarted() == false) {
				System.out.println("Starting scheduler");
				mScheduler.start();
			}
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Scheduler getScheduler() {
		return mScheduler;
	}
}
