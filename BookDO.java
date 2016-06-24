package model;

public class BookDO {
private String title;
private String author;
private String description;
private Integer isbn;
private Integer pages;

	public BookDO(final Integer isbn, final String title,final String author, final String decription, final Integer pages) {
		setIsbn(isbn);
		setTitle(title);
		setAuthor(author);
		setDescription(decription);
		setPages(pages);
		
	}
	
	public BookDO(){
		this(null,null,null,null, null);
	}
	
	public String toString(){
		final String lineSeparator = System.getProperty("line.separator");
		StringBuffer buffer = new StringBuffer(getIsbn()+" "+getAuthor()+  " "+getTitle() +" "+
	                              getDescription()+" "+getPages()+ lineSeparator);
		
		return buffer.toString();
	}
	
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getIsbn() {
		return isbn;
	}
	public void setIsbn(Integer isbn) {
		this.isbn = isbn;
	}
	public Integer getPages() {
		return pages;
	}
	public void setPages(Integer pages) {
		this.pages = pages;
	}

}
