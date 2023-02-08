package org.dragonguard.server.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogUtil {
	
	public static void printInfo(String formatString, Object... args){
		System.out.printf("%s[%s]: %sDragonGuard%s\u00bb%s %s", ColorUtil.RESET, new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()), ColorUtil.GRAY, ColorUtil.DARK_GRAY, ColorUtil.WHITE, String.format(formatString, args) + "\n");
		saveLog(String.format("[%s]: DragonGuard\u00bb %s", new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()), String.format(formatString, args) + "\n"), "info.txt");
	}
	
	public static void printError(String formatString, Object... args){
		System.out.printf("%s[%s]: %sDragonGuard%s\u00bb%s %s", ColorUtil.RESET, new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()), ColorUtil.DARK_RED, ColorUtil.DARK_GRAY, ColorUtil.RED, String.format(formatString, args) + "\n");
		saveLog(String.format("[%s]: DragonGuard\u00bb %s", new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()), String.format(formatString, args) + "\n"), "error.txt");
	}
	
	public static void saveLog(String string, String filename) {
		try {
			File file = new File("logs/" + filename);
			file.getParentFile().mkdirs();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
			string = string.replace("\033[38;2;247;74;74m", "");
			string = string.replace("\033[0;37m", "");
			writer.append(string);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			printError("save log failed.");
		}
	}
}
