/*
 * Copyright (c) 2015 Rodel M. Talampas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * 
 */
package rt.fx.base.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

/**
 * @author rodel.talampas
 * @param <T>
 *
 */
@Transactional
public abstract class ADao<T> implements IDao<T> {
	protected String TABLE_NAME;
	protected String PRIMARY_COLUMN;
	
	protected Map<String, String> COLUMN_MAP = new HashMap<String, String>();
	
	@PersistenceContext
	protected EntityManager em;
	
	protected String retrieveAllQuery() {
		return "SELECT x FROM " + TABLE_NAME + " x ";
	}
	
	protected Query getQuery(){
		return em.createQuery(retrieveAllQuery());
	}
	
	protected abstract String retrieveQuery();
	
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		return (List<T>)getQuery().getResultList(); 
	}
	
	@SuppressWarnings("unchecked")
	public List<T> get(String[] columnIds, Object[] values ){
		
		System.out.println("EM: " + em);
		
		Query qm = em.createQuery(retrieveQuery());
		
		for (int i=0; i<columnIds.length; i++)
			qm.setParameter(columnIds[i], values[i]);
		
		return (List<T>)qm.getResultList();
	}
	
	public T getByPrimary(Object value){
		System.out.println(PRIMARY_COLUMN + " " + value);
		return get(PRIMARY_COLUMN,value).get(0);
	}
	
	public List<T> get(String column, Object value){
		String[] col={column};
		Object[] val={value};
		return get(col,val);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getBySQL(String sql, String[] columnIds, Object[] values) {
		Query qm = em.createQuery(sql);
		for (int i=0; i<columnIds.length; i++)
			qm.setParameter(columnIds[i], values[i]);
		
		return (List<T>)qm.getResultList();
	}
	
	public List<T> getBySQL(String sql, String columnId, Object value) {
		String[] col={columnId};
		Object[] val={value};
		
		return getBySQL(sql,col,val);
	}
	
	public List<T> getBySQL(String sql) {
		String[] col={};
		Object[] val={}; 
		// pass empty arrays
		return getBySQL(sql,col,val);
	}
	
}
