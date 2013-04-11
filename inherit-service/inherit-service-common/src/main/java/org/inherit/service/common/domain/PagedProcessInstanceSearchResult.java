package org.inherit.service.common.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("PagedProcessInstanceSearchResult")
public class PagedProcessInstanceSearchResult implements Serializable {

	private static final long serialVersionUID = 7638210689621188750L;

	List<ProcessInstanceListItem> hits = new ArrayList<ProcessInstanceListItem>();
	int fromIndex;
	int pageSize;
	String sortBy;
	String sortOrder;
	int numberOfHits;

	public PagedProcessInstanceSearchResult() {
		
	}

	public List<ProcessInstanceListItem> getHits() {
		return hits;
	}

	public void setHits(List<ProcessInstanceListItem> hits) {
		this.hits = hits;
	}

	public int getFromIndex() {
		return fromIndex;
	}

	public void setFromIndex(int fromIndex) {
		this.fromIndex = fromIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public int getNumberOfHits() {
		return numberOfHits;
	}

	public void setNumberOfHits(int numberOfHits) {
		this.numberOfHits = numberOfHits;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fromIndex;
		result = prime * result + ((hits == null) ? 0 : hits.hashCode());
		result = prime * result + numberOfHits;
		result = prime * result + pageSize;
		result = prime * result + ((sortBy == null) ? 0 : sortBy.hashCode());
		result = prime * result
				+ ((sortOrder == null) ? 0 : sortOrder.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PagedProcessInstanceSearchResult other = (PagedProcessInstanceSearchResult) obj;
		if (fromIndex != other.fromIndex)
			return false;
		if (hits == null) {
			if (other.hits != null)
				return false;
		} else if (!hits.equals(other.hits))
			return false;
		if (numberOfHits != other.numberOfHits)
			return false;
		if (pageSize != other.pageSize)
			return false;
		if (sortBy == null) {
			if (other.sortBy != null)
				return false;
		} else if (!sortBy.equals(other.sortBy))
			return false;
		if (sortOrder == null) {
			if (other.sortOrder != null)
				return false;
		} else if (!sortOrder.equals(other.sortOrder))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PagedProcessInstanceSearchResult [hits=" + hits
				+ ", fromIndex=" + fromIndex + ", pageSize=" + pageSize
				+ ", sortBy=" + sortBy + ", sortOrder=" + sortOrder
				+ ", numberOfHits=" + numberOfHits + "]";
	}
	
	
}
