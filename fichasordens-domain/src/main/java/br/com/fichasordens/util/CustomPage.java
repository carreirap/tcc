package br.com.fichasordens.util;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class CustomPage<T> extends PageImpl<T> {
	
	private int totalPages;

	public CustomPage(List<T> content, Pageable pageable, long total) {
		super(content, pageable, total);
	}

	@Override
	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + totalPages;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		boolean flag = false;
		if (this == obj)
			flag = true;
		if (!super.equals(obj))
			flag = false;
		if (getClass() != obj.getClass())
			flag = false;
		CustomPage other = (CustomPage) obj;
		if (totalPages != other.totalPages) {
			flag = false;
		}
		return flag;
	}
	
}
