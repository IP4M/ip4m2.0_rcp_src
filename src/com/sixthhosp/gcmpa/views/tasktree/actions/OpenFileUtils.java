package com.sixthhosp.gcmpa.views.tasktree.actions;

import java.io.File;

import org.eclipse.swt.program.Program;

/**
 * ����ϵͳ�򿪳�����ļ���
 * 
 * @author zhengzequn
 *
 */
public class OpenFileUtils {

	/**
	 * ���ļ�����Ŀ¼
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
