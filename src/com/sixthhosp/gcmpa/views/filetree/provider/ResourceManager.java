package com.sixthhosp.gcmpa.views.filetree.provider;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.program.Program;

/**
 * 
 * @author zhengzequn </p> �ļ�������е��ļ�ͼ������� </p>
 */
public class ResourceManager {

	// ���ͼ��ע����
	private static ImageRegistry imageRegistry;

	public static Image getIconByExt(String extension) {
		if (imageRegistry == null) {
			imageRegistry = JFaceResources.getImageRegistry();
		}
		Image image = imageRegistry.get(extension);
		if (image != null)
			return image;

		Program program = Program.findProgram(extension);
		ImageData imageData = (program == null ? null : program.getImageData());
		if (imageData != null) {
			ImageDescriptor imageDescriptor = ImageDescriptor
					.createFromImageData(imageData);
			imageRegistry.put(extension, imageDescriptor);
			image = imageRegistry.get(extension);
			return image;
		} else {
			return null;
		}
	}

}
