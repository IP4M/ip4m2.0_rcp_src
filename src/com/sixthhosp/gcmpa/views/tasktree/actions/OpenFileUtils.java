package com.sixthhosp.gcmpa.views.tasktree.actions;

import java.io.File;

import org.eclipse.swt.program.Program;

/**
 * 调用系统打开程序和文件夹
 * 
 * @author zhengzequn
 *
 */
public class OpenFileUtils {

	/**
	 * 打开文件或者目录
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void opneFile(File file) throws Exception {
		if (file.exists()) {
			if (file.isFile()) {
				boolean isOpenSuccessed = Program
						.launch(file.getAbsolutePath());
				if (!isOpenSuccessed) {
					throw new Exception("Cann't open the file: \""
							+ file.getAbsolutePath() + "\"");
				}
			} else if (file.isDirectory()) {
				boolean isOpenSuccessed = Program
						.launch(file.getAbsolutePath());
				if (!isOpenSuccessed) {
					throw new Exception("Cann't open the folder: \""
							+ file.getAbsolutePath() + "\"");
				}
			}
		}
	}

}
