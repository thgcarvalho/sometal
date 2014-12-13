package br.com.grandev.acesso.util;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadProperties {
	private static final String PROP_FILE_NAME = "socket.properties";

	// local
	public static final String LOCAL_JDBC = "local_JdbcClassName";
	public static final String LOCAL_CONNSTR = "local_ConnectionString";
	public static final String LOCAL_USER = "local_User";
	public static final String LOCAL_PASSWORD = "local_Password";

	// web
	public static final String WEB_JDBC = "web_JdbcClassName";
	public static final String WEB_CONNSTR = "web_ConnectionString";
	public static final String WEB_USER = "web_User";
	public static final String WEB_PASSWORD = "web_Password";

	public static Properties load()
	throws IOException {
		return (load(System.getProperty("user.home")));
	}

	private static Properties load(String strPath)
	throws IOException {
		Properties propMyProp = new Properties();
		if(strPath != null) {
			if(!(strPath.substring(strPath.length() - 1, strPath.length()).equals("\\") ||
					strPath.substring(strPath.length() - 1, strPath.length()).equals("/"))){
				strPath += File.separator;
			}
		}else {
			strPath = "";
		}

		FileInputStream bInpF = new FileInputStream(new File(strPath + PROP_FILE_NAME));
		propMyProp.load(bInpF);
		bInpF.close();

		// check if all properties exists and are not null, except for:
		// Local section.
		checkExists(propMyProp, LOCAL_JDBC, true);
		checkExists(propMyProp, LOCAL_CONNSTR, true);
		checkExists(propMyProp, LOCAL_USER, true);
		checkExists(propMyProp, LOCAL_PASSWORD, true);
		// Web section.
		checkExists(propMyProp, WEB_JDBC, true);
		checkExists(propMyProp, WEB_CONNSTR, true);
		checkExists(propMyProp, WEB_USER, true);
		checkExists(propMyProp, WEB_PASSWORD, true);

		return (propMyProp);
	}

	private static void checkExists(Properties propMyProp, String strPropName,
			boolean blCheckNotEmpty) throws IOException {
		String strProp = propMyProp.getProperty(strPropName);
		if ((strProp == null) || (blCheckNotEmpty && strProp.equals(""))) {
			throw (new IOException("Property: " + strPropName
					+ " was not found on " + PROP_FILE_NAME));
		}
	}

}
