package com.dh.hotblue.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.DateTimePath;

@Component
public class DateUtil {
	public boolean isToday(Timestamp date) {
		boolean result = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Timestamp timestamp = new Timestamp(new Date().getTime());
		if(format.format(timestamp).equals(format.format(date))) result = true;
		return result;
	}
	
	public Timestamp addMonth(int add) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.MONTH, add);
		return new Timestamp(cal.getTimeInMillis());
	}
	
	public Timestamp addDay(int add) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DATE, add);
		return new Timestamp(cal.getTimeInMillis());
	}
	
	public String dateFormat(Timestamp date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public Predicate betweenToday(DateTimePath<Timestamp> date) {
		Timestamp today = new Timestamp(System.currentTimeMillis());
		Timestamp st = Timestamp.valueOf(this.dateFormat(today, "yyyy-MM-dd").concat(" 00:00:00"));
		Timestamp end = Timestamp.valueOf(this.dateFormat(today, "yyyy-MM-dd").concat(" 23:59:59"));
		return date.between(st, end);
	}

}
