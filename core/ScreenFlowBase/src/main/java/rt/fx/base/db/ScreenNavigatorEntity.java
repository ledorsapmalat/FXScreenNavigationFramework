/**
 * 
 */
package rt.fx.base.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author rodel.talampas
 *
 */

@Entity(name="ScreenNavigator")
@Table(name="APP.SCREEN_NAVIGATOR")
public class ScreenNavigatorEntity {

	@Id
	@Column(name="ID")
	private String id;
	@ManyToOne
	@JoinColumn(name="OWNER_ID")
	private ScreenControllerEntity owner;
	@ManyToOne
	@JoinColumn(name="TARGET_ID")
	private ScreenControllerEntity target;
	
	public ScreenControllerEntity getOwner() {
		return owner;
	}
	public void setOwner(ScreenControllerEntity owner) {
		this.owner = owner;
	}
	public ScreenControllerEntity getTarget() {
		return target;
	}
	public void setTarget(ScreenControllerEntity target) {
		this.target = target;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}