/**
 * 
 */
package mdl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author smardine
 */
public class MlListeMessageGrille extends ArrayList<MlMessageGrille> {

	public MlListeMessageGrille() {

	}

	public List<MlMessageGrille> getlist() {
		return this;
	}

	public int getTailleListe() {
		return this.size();
	}

}
