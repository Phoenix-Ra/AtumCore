package me.phoenixra.core.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class Paginator {
	
	private int currentPage;
	private final int sizePerPage;
	private int totalElements;
	private Collection<?> collection;

	public Paginator(int sizePerPage, int totalElements) {
		if (sizePerPage < 1) {
			throw new IllegalArgumentException("sizePerPage cannot be less than 1");
		}
		if (totalElements < 0) {
			throw new IllegalArgumentException("totalElements cannot be less than 0");
		}
		this.sizePerPage = sizePerPage;
		this.totalElements = totalElements;
	}

	public Paginator(int sizePerPage, @NotNull Collection<?> collection) {
		if (sizePerPage < 1) {
			throw new IllegalArgumentException("sizePerPage cannot be less than 1");
		}
		this.sizePerPage = sizePerPage;
		this.collection = collection;
	}

	public int getTotalElements() {
		if (collection != null) {
			return collection.size();
		}
		return totalElements;
	}

	public int getSizePerPage() {
		return sizePerPage;
	}

	public int getMinIndex() {
		return getCurrentPage() * getSizePerPage();
	}

	public int getMaxIndex() {
		return (getCurrentPage() + 1) * getSizePerPage();
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public boolean nextPage() {
		if (!hasNextPage()) {
			return false;
		}
		currentPage++;
		return true;
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean hasNextPage() {
		return !((sizePerPage * (currentPage + 1)) > getTotalElements());
	}

	public boolean previousPage() {
		if (hasPreviousPage()) {
			currentPage--;
			return true;
		}
		return false;
	}

	public boolean hasPreviousPage() {
		return currentPage > 0;
	}

	public boolean isValidIndex(int index) {
        return index >= getMinIndex() && index < getMaxIndex() && index < getTotalElements();
    }
}
