package org.geogebra.desktop.gui.dialog;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import org.geogebra.common.gui.InputHandler;
import org.geogebra.common.gui.dialog.handler.NumberInputHandler;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.kernelND.GeoPointND;
import org.geogebra.common.main.DialogManager;
import org.geogebra.common.util.AsyncOperation;
import org.geogebra.desktop.gui.GuiManagerD;
import org.geogebra.desktop.main.AppD;

/**
 * Dialog for "Segment with given length" tool
 */
public class InputDialogSegmentFixedD extends InputDialogD {

	private GeoPointND geoPoint1;

	private Kernel kernel;

	public InputDialogSegmentFixedD(AppD app, String title,
			InputHandler handler, GeoPointND point1, Kernel kernel) {
		super(app, app.getPlain("Length"), title, "", false, handler);

		geoPoint1 = point1;
		this.kernel = kernel;
	}

	/**
	 * Handles button clicks for dialog.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		try {
			if (source == btOK || source == inputPanel.getTextComponent()) {
				processInput();
			} else if (source == btCancel) {
				setVisibleForTools(false);
			}
		} catch (Exception ex) {
			// do nothing on uninitializedValue
			setVisibleForTools(false);
		}
	}

	private void processInput() {

		// avoid labeling of num
		final Construction cons = kernel.getConstruction();
		final boolean oldVal = cons.isSuppressLabelsActive();
		cons.setSuppressLabelCreation(true);

		inputHandler.processInput(inputPanel.getText(), this,
				new AsyncOperation<Boolean>() {

					@Override
					public void callback(Boolean ok) {
						cons.setSuppressLabelCreation(oldVal);
						if (ok) {
							DialogManager.doSegmentFixed(kernel, geoPoint1,
									((NumberInputHandler) inputHandler)
											.getNum());
						}
						setVisibleForTools(!ok);
					}
				});





	}

	@Override
	public void windowGainedFocus(WindowEvent arg0) {
		if (!wrappedDialog.isModal()) {
			app.setCurrentSelectionListener(null);
		}
		((GuiManagerD) app.getGuiManager()).setCurrentTextfield(this, true);
	}

	public void handleDialogVisibilityChange(boolean isVisible) {

	}
}
