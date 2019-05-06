package com.sixthhosp.gcmpa.views.tooltree.provider;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.sixthhosp.gcmpa.configs.ConfigFile;
import com.sixthhosp.gcmpa.tools.Tool;
import com.sixthhosp.gcmpa.tools.ToolSection;
import com.sixthhosp.gcmpa.tools.ToolSectionLabel;

/**
 * 模块分类树的标签提器
 * 
 * @author zhengzequn
 * 
 */
public class ToolLabelProvider extends StyledCellLabelProvider implements
		ILabelProvider {

	private static String defaultToolImagePath = ConfigFile
			.getProjectFolderPath() + "/" + "icons/toolico.gif";
	private static String defaultSectionImagePath = ConfigFile
			.getProjectFolderPath() + "/" + "icons/flatLayout(3).gif";
	private ArrayList<Image> images = new ArrayList<Image>();
	private Image defaultToolImage;
	private Image defaultSectionImage;
	private Font toolFont;

	public ToolLabelProvider() {
		// TODO Auto-generated constructor stub
		super();
		defaultToolImage = new Image(Display.getCurrent(), defaultToolImagePath);
		defaultSectionImage = new Image(Display.getCurrent(),
				defaultSectionImagePath);

		FontData[] toolFontDatas = Display.getCurrent().getSystemFont()
				.getFontData();
		for (FontData fontData : toolFontDatas) {
			fontData.setStyle(SWT.BOLD);
		}
		toolFont = new Font(Display.getCurrent(), toolFontDatas);
	}

	@Override
	public void update(ViewerCell cell) {
		// TODO Auto-generated method stub
		Object object = cell.getElement();
		if (object instanceof ToolSectionLabel) {
			ToolSectionLabel toolSectionLabel = (ToolSectionLabel) object;
			String label = toolSectionLabel.getText();
			cell.setText(label);
			cell.setForeground(getViewer().getControl().getDisplay()
					.getSystemColor(SWT.COLOR_DARK_GREEN));
			cell.setFont(toolFont);
		} else if (object instanceof Tool) {
			Tool tool = (Tool) object;
			String name = tool.getName();
			String description = tool.getDescription();
			String string = description != null ? name + " " + description
					: name;

			StyleRange styleRange = new StyleRange();
			styleRange.start = 0;
			styleRange.length = name.length();
			styleRange.font = toolFont;
			StyleRange styleRanges[] = { styleRange };

			cell.setText(string);
			cell.setStyleRanges(styleRanges);

			String iconPath = tool.getIconPath();
			if (StringUtils.isNotBlank(iconPath) && new File(iconPath).exists()) {
				Image image = new Image(Display.getCurrent(), iconPath);
				images.add(image);
				cell.setImage(image);
			} else {
				cell.setImage(defaultToolImage);
			}

		} else if (object instanceof ToolSection) {
			ToolSection toolSection = (ToolSection) object;
			String name = toolSection.getName();
			cell.setText(name);

			StyleRange styleRange = new StyleRange();
			styleRange.start = 0;
			styleRange.length = name.length();
			styleRange.underline = true;

			StyleRange styleRanges[] = { styleRange };
			cell.setStyleRanges(styleRanges);

			String icon = toolSection.getIcon();
			String iconPath = ConfigFile.getSoftFolderPath() + "/" + icon;
			if (icon != null && new File(iconPath).exists()) {
				Image image = new Image(Display.getCurrent(), iconPath);
				images.add(image);
				cell.setImage(image);
			} else {
				cell.setImage(defaultSectionImage);
			}
		}
		super.update(cell);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		for (Image image : images) {
			image.dispose();
		}
		defaultToolImage.dispose();
		defaultSectionImage.dispose();
		toolFont.dispose();
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		// TODO Auto-generated method stub
		if (element instanceof ToolSectionLabel) {
			ToolSectionLabel toolSectionLabel = (ToolSectionLabel) element;
			return toolSectionLabel.getText();
		} else if (element instanceof Tool) {
			Tool tool = (Tool) element;
			String description = tool.getDescription();
			String string = description != null ? tool.getName() + " "
					+ description : tool.getName();
			return string;
		} else if (element instanceof ToolSection) {
			ToolSection toolSection = (ToolSection) element;
			return toolSection.getName();
		}
		return null;
	}

	public static String getDefaultToolImagePath() {
		return defaultToolImagePath;
	}

	public static void setDefaultToolImagePath(String defaultToolImagePath) {
		ToolLabelProvider.defaultToolImagePath = defaultToolImagePath;
	}

	public static String getDefaultSectionImagePath() {
		return defaultSectionImagePath;
	}

	public static void setDefaultSectionImagePath(String defaultSectionImagePath) {
		ToolLabelProvider.defaultSectionImagePath = defaultSectionImagePath;
	}

}
