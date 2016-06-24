package model;

import java.sql.Connection;
import java.util.List;

public interface DAO {

	
	public void add(final BookDO book)throws BookException;
	public void update(final BookDO book)throws BookException;
	public void remove(final BookDO book)throws BookException;
	public BookDO find (final BookDO book)throws BookException;
	public BookDO findByTitleAndAuthor(final BookDO book)throws BookException;
	public List<BookDO> findAll()throws BookException;
	
	public Connection getConnection();
	public void setConnection (Connection c);
	public String getConnectionName();
	public void setConnectionName (String name);
	public String getDriverName();
	public void setDriverName(final String name);
	
}
