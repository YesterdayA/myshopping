package demo.wh.admin.commons;

import java.util.List;

public class TableData<T> {
    private Integer total;
    private List<T> rows;

    public TableData() {
    }

    public TableData(Integer total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
