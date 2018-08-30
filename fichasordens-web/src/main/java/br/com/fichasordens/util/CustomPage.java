package br.com.fichasordens.util;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class CustomPage<T> extends PageImpl<T> {
	
	private int totalPages;

	public CustomPage(List<T> content, Pageable pageable, long total) {
		super(content, pageable, total);
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	
	
}
