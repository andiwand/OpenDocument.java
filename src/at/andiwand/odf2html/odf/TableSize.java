package at.andiwand.odf2html.odf;

public class TableSize {
	
	private final int columns;
	private final int rows;
	
	public TableSize(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + columns;
		result = prime * result + rows;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		TableSize other = (TableSize) obj;
		if (columns != other.columns) return false;
		if (rows != other.rows) return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TableSize [columns=");
		builder.append(columns);
		builder.append(", rows=");
		builder.append(rows);
		builder.append("]");
		return builder.toString();
	}
	
	public int getColumns() {
		return columns;
	}
	
	public int getRows() {
		return rows;
	}
	
}