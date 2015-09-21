/**
 * 
 */
package rt.fx.base.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author rodel.talampas
 *
 */

@Entity(name="ScreenController")
@Table(name="APP.SCREEN_CONTROLLER")
public class ScreenControllerEntity {

	@Id
	@Column(name="ID")
	private String id;
	@Column(name="FILE_CONTEXT")
	private String fileContext;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileContext() {
		return fileContext;
	}
	public void setFileContext(String fileContext) {
		this.fileContext = fileContext;
	}
	
}
