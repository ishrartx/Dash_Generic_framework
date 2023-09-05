package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.Status;

/**
 * The type Log util.
 *
 * @author TestLink
 */
public class LogUtil extends ExtentUtil {

	/**
	 * Instantiates a new Log util.
	 */
	public LogUtil() {
	}

	/** The Constant logger. */
	private static final ThreadLocal<Logger> logger = new ThreadLocal<Logger>();

	/**
	 * Gets the logger.
	 *
	 * @return the logger
	 */
	private static ThreadLocal<Logger> getLogger() {
		return logger;
	}

	/**
	 * Sets the logger.
	 *
	 * @param c
	 *        the new logger
	 */
	private static void setLogger(Class<?> c) {
		logger.set(LogManager.getLogger(c));
	}

	/**
	 * Info log.
	 *
	 * @param clazz
	 *        the clazz
	 * @param message
	 *        the message
	 */
	public static void infoLog(Class<?> clazz, String message) {
		setLogger(clazz);
		getLogger().get().info(message);

	}

	/**
	 * Error log.
	 *
	 * @param clazz
	 *        the clazz
	 * @param message
	 *        the message
	 * @param t
	 *        the t
	 */
	public static void errorLog(Class<?> clazz, String message, Throwable t) {
		setLogger(clazz);
		getLogger().get().error(message, t);
		getLogger().get().error("----------------------------------------------------------------------");

	}

	/**
	 * Error log.
	 *
	 * @param clazz
	 *        the clazz
	 * @param message
	 *        the message
	 */
	public static void errorLog(Class<?> clazz, String message) {
		setLogger(clazz);
		getLogger().get().error(message);
		getLogger().get().error("-----------------------------------------------------------------------");

	}

	/**
	 * Html info log.
	 *
	 * @param message
	 *        the message
	 */
	public static void htmlInfoLog(String message) {
		ExtentUtil.logger.get().log(Status.INFO, HTMLReportUtil.infoStringBlueColor(message));
	}

	/**
	 * Html pass log.
	 *
	 * @param message
	 *        the message
	 */
	public static void htmlPassLog(String message) {
		ExtentUtil.logger.get().log(Status.PASS, HTMLReportUtil.passStringGreenColor(message));
	}

	/**
	 * Html fail log.
	 *
	 * @param message
	 *        the message
	 */
	public static void htmlFailLog(String message) {
		ExtentUtil.logger.get().log(Status.FAIL, HTMLReportUtil.failStringRedColor(message));
	}

	/**
	 * Html warning log.
	 *
	 * @param message
	 *        the message
	 */
	public static void htmlWarningLog(String message) {
		ExtentUtil.logger.get().log(Status.WARNING, HTMLReportUtil.warningStringOrangeColor(message));
	}

}
