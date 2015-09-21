/**
 * 
 */
package rt.fx.base.db;


/**
 * @author rodel.talampas
 *
 */
public class ScreenNavigatorDaoImpl extends ADao<ScreenNavigatorEntity> {
	
	public ScreenNavigatorDaoImpl(){
		TABLE_NAME="ScreenNavigator";
		PRIMARY_COLUMN = "ID";
	}

	@Override
	protected String retrieveQuery() {
		return retrieveAllQuery() + " WHERE "
				+ " x.id = :"
				+ PRIMARY_COLUMN;
	}

	
}
