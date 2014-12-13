package br.com.grandev.acesso.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DisplayMessage {
	public static void display(String strWho, String strMessage) {
		Calendar calNow = Calendar.getInstance();
		SimpleDateFormat sdfYMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdfYMDHMS.format(calNow.getTime()) + " " + strWho + ": " + strMessage);
	}
}
