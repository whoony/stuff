/**
 * Copyright (C) 2014-2015 5WeHealth Technologies. All rights reserved.
 *  
 *    @author: Jingtao Yun Jun 1, 2015
 */

package com.stuff.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;


/**
 * This class is to help build query string and return model list.
 */
public class QueryBuilder 
{

	public static class OrderClause 
	{

		private String _property;
		
		private boolean _isAsc = true;
		
		private boolean _needAlias = true;
		
		public OrderClause(String property)
		{
			_property = property;
		}
		
		public OrderClause(String property, boolean isAsc)
		{
			_property = property;
			_isAsc = isAsc;
		}
		
		public static OrderClause valueOf(String property, boolean isAsc)
		{
			return new OrderClause(property, isAsc);
		}
		
		public String getProperty()
		{
			return _needAlias ? _alias + "." + _property : _property;
		}
		
		public String getOrder()
		{
			return _isAsc ? "ASC" : "DESC";
		}
		
		public String buildClause()
		{
			return getProperty() + " " + getOrder();
		}
		
		public void setNeedAlias(boolean needAlias)
		{
			_needAlias = needAlias;
		}
	}
	
	public static class PropertyClause
	{
		private String _property;
		
		private String _operator;
		
		private Object _value;
		/** used to add named query ( where username=:propAlias ) */
		private String _propAlias;
		private boolean _needAlias = true;
		
		public PropertyClause(String property, String operator, Object value)
		{
			_property = property;
			_operator = operator;
			_value = value;
		}
		
		public static PropertyClause valueOf(String property, String operator, Object value)
		{
			return new PropertyClause(property, operator, value);
		}
		
		public void setNeedAlias(boolean needAlias)
		{
			_needAlias = needAlias;
		}
		
		public String getProperty()
		{
			return _needAlias ? _alias + "." + _property : _property;
		}
		
		public String getOperator()
		{
			return _operator;
		}
		
		public Object getValue()
		{
			if("like".equalsIgnoreCase(_operator))
				return "%" + _value + "%";
			else if(_operator.contains("is"))
				return "";
			else
				return _value;
		}
		
		public void setPropAlias(String propAlias)
		{
			_propAlias = propAlias;
		}
		
		public String getPropAlias()
		{
			return _propAlias;
		}
		
		public String buildClause()
		{
			if(_propAlias == null)
				_propAlias = _alias + _property;
			
			if(_operator.contains("is"))
			{
				return getProperty() + " " +  getOperator();
			}
			
			return getProperty() + " " +  getOperator() +  " :" + getPropAlias();
		}
		
		public boolean hasValue()
		{
			return _value != null;
		}
	}

	public static class UnionPropertyClause
	{
		private PropertyClause one;
		private PropertyClause two;
		/** AND, OR */
		private String operator;
		
		public static UnionPropertyClause valueOf(PropertyClause one, String operator, PropertyClause two)
		{
			UnionPropertyClause union = new UnionPropertyClause();
			union.one = one;
			union.two = two;
			union.operator = operator;
			return union;
		}
		
		public PropertyClause getOne()
		{
			return one;
		}
		
		public String getOperator()
		{
			return " " + operator + " ";
		}
		
		public PropertyClause getTwo()
		{
			return two;
		}
	}
	
	public static class PageClause
	{
		/** start from 0 */
		private int _page;
		
		private int _pageCount;
		
		public PageClause(int page, int pageCount)
		{
			_page = page;
			_pageCount = pageCount;
		}
		
		public int getStartNumber()
		{
			return _page * _pageCount;
		}
		
		public int getPageCount()
		{
			return _pageCount;
		}
		
	}
	
	public static class SelectClause
	{
		private String _clause;
		
		private boolean _isDistinct = false;
		private boolean _needAlias = false;
		
		public SelectClause(String clause)
		{
			_clause = clause;
		}
		
		public void setDistinct(boolean isDistinct)
		{
			_isDistinct = isDistinct;
		}
		
		public void setNeedAlias(boolean needAlias)
		{
			_needAlias = needAlias;
		}
		
		public static SelectClause valueOf(String clause, boolean isDistinct, boolean needAlias)
		{
			SelectClause selectClause = new SelectClause(clause);
			selectClause.setDistinct(isDistinct);
			selectClause.setNeedAlias(needAlias);
			return selectClause;
		}
		
		public String buildClause() 
		{
			StringBuffer sb = new StringBuffer();
			if(_isDistinct)
				sb.append(" DISTINCT (");
			if(_needAlias)
				sb.append(_alias).append(".");
			sb.append(_clause);
			if(_isDistinct)
				sb.append(") ");
			return sb.toString();
		}
		
	}
	
	/** The alias. */
	private static String _alias = "model";
	/** The Model class. */
	private Class _clazz;
	/** Clause after where, the key is property, the value is the value. */
	private List<PropertyClause> _properties  = new ArrayList<PropertyClause>();
	/** (clause1 and clause2)  (clause1 or clause2) */
	private List<UnionPropertyClause> _unions = new ArrayList<UnionPropertyClause>();
	/** Order Clause. */
	private List<OrderClause> _orders = new ArrayList<OrderClause>();
	/** In Mysql page is using limit m,n .*/
	private PageClause _pageClause;
	/** Select Clause */
	private SelectClause _selectClause;
	
	/**
	 * Constructor
	 * @param clazz the Model class
	 */
	public QueryBuilder(Class clazz)
	{
		_clazz = clazz;
	}
	
	public QueryBuilder addPropertyClause(PropertyClause clause)
	{
		_properties.add(clause);
		return this;
	}
	
	public QueryBuilder addUnionPropertyClause(UnionPropertyClause union)
	{
		_unions.add(union);
		return this;
	}
	
	public QueryBuilder addOrderClause(OrderClause order)
	{
		_orders.add(order);
		return this;
	}
	
	public QueryBuilder addPageClause(PageClause page)
	{
		_pageClause = page;
		return this;
	}
	
	public String buildQuery()
	{
		StringBuffer sb = new StringBuffer("");
		if(null != _selectClause)
			sb.append("SELECT ").append(_selectClause.buildClause());
		
		sb.append(" From ")
			.append(_clazz.getName()).append(" AS ").append(_alias).append(" ");
		
		boolean hasProperty = false;
		if(!_properties.isEmpty())
		{
			hasProperty = true;
			for(int i = 0 ; i < _properties.size(); i++)
			{
				if(i == 0)
					sb.append(" WHERE ");
				else
					sb.append(" AND ");
				PropertyClause prop = _properties.get(i);
				prop.setPropAlias(_alias + "prop_" + i);
				sb.append(prop.buildClause());
			}
		}
		
		if(!_unions.isEmpty())
		{
			sb.append(hasProperty ? " AND " : " WHERE ");
			
			for(int i = 0 ; i < _unions.size(); i++)
			{
				UnionPropertyClause prop = _unions.get(i);
				PropertyClause one = prop.getOne();
				PropertyClause two = prop.getTwo();
				String operator = prop.getOperator();
				
				one.setPropAlias(_alias + "union_prop_one_" + i);
				two.setPropAlias(_alias + "union_prop_two_" + i);
				
				sb.append("(").append(one.buildClause()).append(operator).append(two.buildClause()).append(")");
			}
			
		}
		
		if(!_orders.isEmpty())
		{
			for(int i = 0 ; i < _orders.size(); i++)
			{
				if(i == 0)
					sb.append(" ORDER BY ");
				else
					sb.append(" , ");
				
				OrderClause order = _orders.get(i);
				sb.append(order.buildClause());
			}
		}
		
		return sb.toString();
	}
	
	
	
	public List getList()
	{
		try {
			Session session = getSession();
			Query query = _buildQuery(session);
			List ret = query.list();
			return ret;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public Object uniqueResult()
	{
		try {
			Session session = getSession();
			Query query = _buildQuery(session);
			Object ret = query.uniqueResult();
			return ret;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	
	}
	
	private Query _buildQuery(Session session)
	{
		Query query = session.createQuery(buildQuery());
		for(PropertyClause prop : _properties)
		{
			if(prop.getValue() instanceof Collection)
			{
				query.setParameterList(prop.getPropAlias(), (Collection)prop.getValue());
			}
			else
			{
				if(prop.hasValue())
				query.setParameter(prop.getPropAlias(), prop.getValue());
			}
		}
		
		for(UnionPropertyClause union : _unions)
		{
			PropertyClause one = union.getOne();
			if(one.hasValue())
				query.setParameter(one.getPropAlias(), one.getValue());
			PropertyClause two = union.getTwo();
			if(two.hasValue())
				query.setParameter(two.getPropAlias(), two.getValue());
		}
		
		if(_pageClause != null)
		{
			query.setFirstResult(_pageClause.getStartNumber());
			query.setMaxResults(_pageClause.getPageCount());
		}
		return query;
	}
	
	public void setSelectClause(SelectClause selectClause)
	{
		_selectClause = selectClause;
	}
	
	public String getAlias()
	{
		return _alias;
	}
	
	public Session getSession()
	{
		return HibernateSessionFactory.getSession();
	}
}
