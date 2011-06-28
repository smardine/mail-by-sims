package fenetre.principale;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import tools.OpenWithDefaultViewer;

public class MlActionHtmlPane implements HyperlinkListener {

	@Override
	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			OpenWithDefaultViewer
					.launchBrowser(event.getURL().toExternalForm());
			// htmlPane.setPage(event.getURL());
			// urlField.setText(event.getURL().toExternalForm());

		}

	}

}
