package com.sixthhosp.gcmpa.views.filetree.provider;

import java.io.File;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.sixthhosp.gcmpa.configs.ConfigFile;

/**
 * 文件浏览器标签提供器
 * 
 * @author zhengzequn
 * 
 */
public class FileLabelProvider extends LabelProvider {

	private static String defaultFileImagePath = ConfigFile
			.getProjectFolderPath() + "/" + "icons/unknown_file.png";
	private static String defaultFolderImagePath = ConfigFile
			.getProjectFolderPath() + "/" + "icons/directory.png";

	private static Clipboard clipboard;

	// private static String defaultFileImagePath = "icons/unknown_file.png";
	// private static String defaultFolderImagePath = "icons/directory.png";

	private Image defautFileImage;
	private Image defautFolderImage;

	public FileLabelProvider() {
		// TODO Auto-generated constructor stub
		super();
		defautFileImage = new Image(Display.getCurrent(), defaultFileImagePath);
		defautFolderImage = new Image(Display.getCurrent(),
				defaultFolderImagePath);
	}

	@Override
	public Image getImage(Object element) {
		return getIcon((File) element);
	}

	@Override
	public String getText(Object element) {
		String fileName = ((File) element).getName();
		if (fileName.length() > 0) {
			return fileName;
		}
		return ((File) element).getPath();
	}

	/**
	 * 获取每个文件节点的图标
	 * 
	 * @param file
	 * @return
	 */
	private Image getIcon(File file) {
		if (file.isDirectory())
			return defautFolderImage;
		int lastDotPos = file.getName().lastIndexOf('.');
		if (lastDotPos == -1)
			return defautFileImage;
		Image image = ResourceManager.getIconByExt(file.getName()
				.substring(lastDotPos + 1).toLowerCase());
		return image == null ? defautFileImage : image;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		defautFileImage.dispose();
		defautFolderImage.dispose();
	}

	public static String getDefaultFileImagePath() {
		return defaultFileImagePath;
	}

	public static void setDefaultFileImagePath(String defaultFileImagePath) {
		FileLabelProvider.defaultFileImagePath = defaultFileImagePath;
	}

	public static String getDefaultFolderImagePath() {
		return defaultFolderImagePath;
	}

	public static void setDefaultFolderImagePath(String defaultFolderImagePath) {
		FileLabelProvider.defaultFolderImagePath = defaultFolderImagePath;
	}

	/**
	 * 获取剪切板
	 * 
	 * @return
	 */
	public static Clipboard getClipboard() {
		if (clipboard == null) {
			clipboard = new Clipboard(Display.getCurrent());
		}
		return clipboard;
	}
}
