package com.sixthhosp.gcmpa.editors.ms;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;

import com.sixthhosp.gcmpa.editors.ei.HelpEditorInput;

public class HelpEditorMachingStrategy implements IEditorMatchingStrategy {

	@Override
	public boolean matches(IEditorReference editorRef, IEditorInput input) {
		try {
			IEditorInput refInput = editorRef.getEditorInput();
			if (refInput instanceof HelpEditorInput
					&& input instanceof HelpEditorInput) {
				HelpEditorInput refHelpEditorInput = (HelpEditorInput) refInput;
				HelpEditorInput helpEditorInput = (HelpEditorInput) input;
				if (refHelpEditorInput.getTool().equals(
						helpEditorInput.getTool())) {
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
