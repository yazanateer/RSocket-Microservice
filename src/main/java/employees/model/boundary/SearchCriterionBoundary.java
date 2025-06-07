package employees.model.boundary;

public class SearchCriterionBoundary {
    private String searchCriteria;
    private String value;
    private int page = 0;
    private int size = 10;

    public SearchCriterionBoundary() {}

    public SearchCriterionBoundary(String searchCriteria, String value, int page, int size) {
        this.searchCriteria = searchCriteria;
        this.value = value;
        this.page = page;
        this.size = size;
    }

    // Getters & Setters
    public String getSearchCriteria() { return searchCriteria; }
    public void setSearchCriteria(String searchCriteria) { this.searchCriteria = searchCriteria; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}