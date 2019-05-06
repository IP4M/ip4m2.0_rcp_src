package com.sixthhosp.gcmpa;

import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;

import com.sixthhosp.gcmpa.configs.ConfigFile;

public class Utils {
	public static boolean isAdmin() {
		File dir = new File(System.getenv("SystemRoot"));
		File testFile = null;
		try {
			testFile = File.createTempFile("write", ".dll", dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			if (testFile != null) {
				testFile.delete();
			}
		}
		return true;
	}

	public static boolean isUserNameOK() {
		String name = new File(System.getProperty("user.home"))
				.getAbsolutePath();
		return StringUtils.isAsciiPrintable(name);
	}

	public static boolean isSoftPathOK() {
		String name = ConfigFile.getSoftFolderPath();
		return StringUtils.isAsciiPrintable(name);
	}

	public static boolean isWin64bit() {
		String arch = System.getenv("PROCESSOR_ARCHITECTURE");
		String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

		String realArch = arch != null && arch.endsWith("64")
				|| wow64Arch != null && wow64Arch.endsWith("64") ? "64" : "32";
		if (realArch.equals("64")) {
			return true;
		} else {
			return false;
		}
	}
}
