/**
 * 
 */
package rt.fx.base.db;


/**
 * @author rodel.talampas
 *
 */
public class ScreenControllerDaoImpl extends ADao<ScreenControllerEntity> {
	
	public ScreenControllerDaoImpl(){
		TABLE_NAME="ScreenController";
		PRIMARY_COLUMN = "ID";
	}

	@Override
	protected String retrieveQuery() {
		return retrieveAllQuery() + " WHERE "
				+ " x.id = :"
				+ PRIMARY_COLUMN;
	}

	
}
