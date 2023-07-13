package br.com.trier.springvespertino.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

	private static DateTimeFormatter dtfBR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public static ZonedDateTime strToZonedDateTime(String dateStr) {
		return dateStr != null ? LocalDate.parse(dateStr, dtfBR).atStartOfDay(ZoneId.systemDefault())
				: ZonedDateTime.now();
	}

	public static String zonedDateTimeToStr(ZonedDateTime date) {
		return date != null ? dtfBR.format(date) : "";
	}

}
