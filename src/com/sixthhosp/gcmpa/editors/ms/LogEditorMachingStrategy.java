package com.sixthhosp.gcmpa.editors.ms;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;

import com.sixthhosp.gcmpa.editors.ei.LogEditorInput;

public class LogEditorMachingStrategy implements IEditorMatchingStrategy {

	@Override
	public boolean matches(IEditorReference editorRef, IEditorInput input) {
		try {
			IEditorInput refInput = editorRef.getEditorInput();
			if (refInput instanceof LogEditorInput
					&& input instanceof LogEditorInput) {
				LogEditorInput refLogEditorInput = (LogEditorInput) refInput;
				LogEditorInput logEditorInput = (LogEditorInput) input;
				if (refLogEditorInput.getXmlTask().getId()
						.equals(logEditorInput.getXmlTask().getId())) {
					return true;
				} else {
					return false;
				}
			}
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
