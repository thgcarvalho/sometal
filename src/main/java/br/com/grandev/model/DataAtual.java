package br.com.grandev.model;

import java.util.Calendar;

public class DataAtual implements Data {

	@Override
	public Calendar hoje() {
		return Calendar.getInstance();
	}

}
