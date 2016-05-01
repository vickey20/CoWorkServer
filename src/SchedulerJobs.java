import static org.quartz.TriggerBuilder.newTrigger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

public class SchedulerJobs implements Job{

	public static String REQUEST_TYPE = "REQUEST_TYPE";
	public static final int UPDATE_COWORK_TO_EXPIRE = 1;
	public static String COWORK_ID = "COWORK_ID";
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		
		int requestType = jobDataMap.getInt(REQUEST_TYPE);
		
		System.out.println("Got job request: " + requestType);
		
		handleRequest(requestType, jobDataMap);
	}
	
	private void handleRequest(int requestType, JobDataMap jobDataMap) {
		switch (requestType) {
			case UPDATE_COWORK_TO_EXPIRE:
				int coworkID = jobDataMap.getInt(COWORK_ID);
				DatabaseClass.updateCoworkToExpire(coworkID);
				break;

			default:
				break;
		}
	}
	
	public static void scheduleCoworkToExpire(int coworkID, String time, String date, long duration) {
		String timeDate = date + " " + time;
		
		long startMillis = getTimeInMillis(timeDate, 
				Constants.TimeAndDate.DATE_FORMAT + " " + Constants.TimeAndDate.TIME_FORMAT);
		
		System.out.println("Time in millis: " + startMillis);
		
		long endMillis = startMillis + duration;
		
		Date endDate = new Date(endMillis);
		
		System.out.println("Date end: " + endDate);
		
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put(REQUEST_TYPE, UPDATE_COWORK_TO_EXPIRE);
		jobDataMap.put(COWORK_ID, coworkID);
		
		JobDetail jobDetail = JobBuilder.newJob(SchedulerJobs.class)
				.withIdentity(COWORK_ID)
				.setJobData(jobDataMap)
				.build();
		
		Trigger trigger = newTrigger()
				.withIdentity(COWORK_ID, String.valueOf(coworkID))
				.startAt(endDate)
				.build();
		
		try {
			ServletContextClass.getScheduler().scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static long getTimeInMillis(String timeDateString, String dateFormat) {
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            Date mDate = sdf.parse(timeDateString);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }
}