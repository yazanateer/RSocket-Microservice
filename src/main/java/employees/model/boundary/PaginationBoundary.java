package employees.model.boundary;

public class PaginationBoundary {
    private int page = 0;
    private int size = 10;

    public PaginationBoundary() {}

    public PaginationBoundary(int page, int size) {
        this.page = page;
        this.size = size;
    }

    // Getters & Setters
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}