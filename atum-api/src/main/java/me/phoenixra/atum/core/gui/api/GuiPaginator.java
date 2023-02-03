package me.phoenixra.atum.core.gui.api;

import lombok.Getter;
import lombok.Setter;

public class GuiPaginator {
    @Getter private final int elementsPerPage;

    @Getter @Setter private int totalElements;

    @Getter @Setter
    private int currentPage;
    public GuiPaginator(int elementsPerPage, int totalElements) {
        if (elementsPerPage < 1) {
            throw new IllegalArgumentException("elementsPerPage cannot be less than 1");
        }
        if (totalElements < 0) {
            throw new IllegalArgumentException("totalElements cannot be less than 0");
        }
        this.elementsPerPage = elementsPerPage;
        this.totalElements = totalElements;
    }
    public boolean nextPage() {
        if (!hasNextPage()) {
            return false;
        }
        currentPage++;
        return true;
    }
    public boolean previousPage() {
        if (hasPreviousPage()) {
            currentPage--;
            return true;
        }
        return false;
    }

    public int getMinIndex() {
        return getCurrentPage() * getElementsPerPage();
    }

    public int getMaxIndex() {
        return (getCurrentPage() + 1) * getElementsPerPage();
    }

    public boolean hasNextPage() {
        return !((getElementsPerPage() * (getCurrentPage() + 1)) > getTotalElements());
    }
    public boolean hasPreviousPage() {
        return getCurrentPage() > 0;
    }

    public boolean isValidIndex(int index) {
        return index >= getMinIndex() && index < getMaxIndex() && index < getTotalElements();
    }
}
