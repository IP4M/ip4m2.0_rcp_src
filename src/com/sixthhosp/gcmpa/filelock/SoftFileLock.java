package com.sixthhosp.gcmpa.filelock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

import org.apache.commons.io.FileUtils;

import com.sixthhosp.gcmpa.configs.ConfigFile;

public class SoftFileLock {
	/**
	 * 文件锁File对象
	 */
	private File lockFile;

	public SoftFileLock() {
		lockFile = new File(ConfigFile.getLockFilePath());
		File parentFile = lockFile.getParentFile();
		try {
			if (!parentFile.exists()) {
				FileUtils.forceMkdir(parentFile);
			}
			if (!lockFile.exists()) {
				lockFile.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 加锁
	 * 
	 * @return
	 */
	@SuppressWarnings("resource")
	public boolean lock() {
		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile(lockFile,
					"rw");
			FileChannel fileChannel = randomAccessFile.getChannel();
			FileLock fileLock = fileChannel.tryLock();
			if (fileLock != null) {
				return true;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OverlappingFileLockException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 确认是否已经启动了程序
	 * 
	 * @return
	 */
	public static boolean isLocked() {
		SoftFileLock softFileLock = new SoftFileLock();
		return softFileLock.lock();
	}
}
