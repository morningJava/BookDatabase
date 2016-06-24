/**
 * Implements the Book Database.
 */

package view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.BookDO;
import model.BookException;
import model.DAO;

public class BookDAOImpl implements DAO {

	// A wild Logger appeared! (using log4j, obtained from apache.org)
	private static final Logger logger = LogManager
			.getLogger(BookDAOImpl.class);// to help fix stuff later,
											// because it'll break
											// atleast once
	// Database variables
	private static final String DEFAULT_DRIVER = "org.hsqldb.jdbcDriver";
	private static final String DEFAULT_CONNECTION = "jdbc:hsqldb:hsql://localhost/mydatabase";

	private Connection connection;
	private String driverName;
	private String connectionName;

	// SQL variables for prepared statements
	private final String UPDATE = "UPDATE BOOK "
			+ "SET TITLE = ?, AUTHOR = ?, DESCRIPTION = ?, PAGES = ? "
			+ " WHERE ISBN = ?";
	private final String INSERT = "INSERT INTO BOOK "
			+ "(ISBN, TITLE, AUTHOR, DESCRIPTION, PAGES) "
			+ " VALUES(?, ?, ?, ?, ?)";
	private final String SELECT_BY_TITLE_AUTHOR = "SELECT "
			+ "ISBN, TITLE, AUTHOR, DESCRIPTION, PAGES "
			+ "FROM BOOK " + "WHERE TITLE = ? AND AUTHOR = ?";
	private final String SELECT = "SELECT "
			+ "ISBN, TITLE, AUTHOR, DESCRIPTION, PAGES "
			+ "FROM BOOK " + "WHERE ISBN = ?";
	private final String SELECT_ALL = "SELECT "
			+ "ISBN, TITLE, AUTHOR, DESCRIPTION, PAGES"
			+ " FROM BOOK";
	private final String DELETE = "DELETE FROM BOOK WHERE ISBN = ?";

	/**
	 * Connects to the database.
	 * 
	 * @param driverName
	 * @param connectionName
	 * @throws BookException
	 */
	public BookDAOImpl(final String driverName,
			final String connectionName) throws BookException {

		this.driverName = driverName;
		this.connectionName = connectionName;
		initializeDB(driverName, connectionName);
	}

	/**
	 * Constructor.
	 */
	public BookDAOImpl() throws BookException {

		this(DEFAULT_DRIVER, DEFAULT_CONNECTION);
	}

	/**
	 * Builds prepared statements.
	 * 
	 * @param preparedStatement
	 * @return
	 * @throws BookException
	 */
	private PreparedStatement prepareStatement(
			String preparedStatement) throws BookException {

		PreparedStatement statement;
		try {
			statement = connection
					.prepareStatement(preparedStatement);
		}
		catch (SQLException e) {
			throw new BookException(
					"Unable to create prepared statement "
							+ preparedStatement,
					e);
		}
		return statement;
	}

	/**
	 * Update the database.
	 * 
	 * @param statement
	 * @throws BookException
	 */
	private void executeUpdate(PreparedStatement statement)
			throws BookException {

		try {
			statement.executeUpdate();
		}
		catch (SQLException e) {
			throw new BookException("Unable to execute statement ",
					e);
		}
	}

	/**
	 * Executes a query statement
	 * 
	 * @param statement
	 * @return
	 * @throws BookException
	 */
	private ResultSet executeQuery(PreparedStatement statement)
			throws BookException {

		ResultSet result;
		try {
			result = statement.executeQuery();
		}
		catch (SQLException e) {
			throw new BookException(
					"Unable to execute  query " + SELECT, e);
		}
		return result;
	}

	/**
	 * Adds a new book to the data base
	 */
	public void add(BookDO book) throws BookException {

		PreparedStatement statement = prepareStatement(INSERT);
		try {
			statement.setInt(1, book.getIsbn());
			statement.setString(2, book.getTitle());
			statement.setString(3, book.getAuthor());
			statement.setString(4, book.getDescription());
			statement.setInt(5, book.getPages());

		}
		catch (SQLException e) {
			throw new BookException("Unable to add book", e);
		}
		executeUpdate(statement);
	}

	/**
	 * Updates a book's information.
	 */
	public void update(BookDO book) throws BookException {

		PreparedStatement statement = prepareStatement(UPDATE);
		try {
			statement.setString(1, book.getTitle());
			statement.setString(2, book.getAuthor());
			statement.setString(3, book.getDescription());
			statement.setInt(4, book.getPages());
			statement.setInt(5, book.getIsbn());
		}
		catch (SQLException e) {
			throw new BookException(
					"Unable to set prepared values for statement ",
					e);
		}

		executeUpdate(statement);

	}

	/**
	 * Removes a book from the data base.
	 */
	public void remove(BookDO book) throws BookException {

		PreparedStatement statement = prepareStatement(DELETE);
		try {
			statement.setInt(1, book.getIsbn());
		}
		catch (SQLException e) {
			throw new BookException("Unable to remove book", e);
		}
		executeUpdate(statement);
	}

	/**
	 * Searches for a Book object in the data base.
	 */
	public BookDO find(BookDO book) throws BookException {

		PreparedStatement statement = prepareStatement(SELECT);
		try {
			statement.setInt(1, book.getIsbn());
		}
		catch (SQLException e) {
			throw new BookException(
					"Unable to set prepared values for statement "
							+ SELECT,
					e);
		}

		ResultSet rs = executeQuery(statement);

		try {
			if (rs.next()) {
				book.setIsbn(rs.getInt("ISBN"));
				book.setTitle(rs.getString("TITLE"));
				book.setAuthor(rs.getString("AUTHOR"));
				book.setDescription(rs.getString("DESCRIPTION"));
				book.setPages(rs.getInt("PAGES"));
			}
		}
		catch (SQLException e) {
			throw new BookException(
					"Unable to retrieve row data from results for statement "
							+ SELECT,
					e);
		}
		finally {
			try {
				rs.close();
			}
			catch (SQLException e) {
				logger.warn("Error closing result set for select "
						+ SELECT);
			}
		}
		return book;
	}

	/**
	 * Searches for a book object by the title and author fields.
	 */
	public BookDO findByTitleAndAuthor(BookDO book)
			throws BookException {

		PreparedStatement statement = prepareStatement(
				SELECT_BY_TITLE_AUTHOR);
		try {
			statement.setString(1, book.getTitle());
			statement.setString(2, book.getAuthor());
		}
		catch (SQLException e) {
			throw new BookException(
					"Unable to set prepared values for statement "
							+ SELECT,
					e);
		}

		ResultSet rs = executeQuery(statement);

		try {
			if (rs.next()) {
				book.setIsbn(rs.getInt("ISBN"));
				book.setTitle(rs.getString("TITLE"));
				book.setAuthor(rs.getString("AUTHOR"));
				book.setDescription(rs.getString("DESCRIPTION"));
				book.setPages(rs.getInt("PAGES"));
			}
		}
		catch (SQLException e) {
			throw new BookException(
					"Unable to retrieve row data from results for statement "
							+ SELECT,
					e);
		}
		finally {
			try {
				rs.close();
			}
			catch (SQLException e) {
				logger.warn("Error closing result set for select "
						+ SELECT);
			}
		}
		return book;
	}

	/**
	 * Returns all book in the database as a List.
	 */
	public List<BookDO> findAll() throws BookException {

		final List<BookDO> books = new ArrayList<BookDO>();
		BookDO book;

		final PreparedStatement statement = prepareStatement(
				SELECT_ALL);

		ResultSet rs = executeQuery(statement);

		try {
			while (rs.next()) {
				book = new BookDO(rs.getInt("ISBN"),
						rs.getString("TITLE"), rs.getString("AUTHOR"),
						rs.getString("DESCRIPTION"),
						rs.getInt("PAGES"));
				books.add(book);
			}
		}
		catch (SQLException e) {
			throw new BookException(
					"Unable to retrieve row data from results for statement "
							+ SELECT_ALL,
					e);
		}
		finally {
			try {
				rs.close();
			}
			catch (SQLException e) {
				logger.warn("Error closing result set for select "
						+ SELECT_ALL);
			}
		}
		return books;
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {

		return connection;
	}

	/**
	 * @param connection
	 *            the connection to set
	 */
	public void setConnection(final Connection connection) {

		this.connection = connection;
	}

	/**
	 * @return the driverName
	 */
	public String getDriverName() {

		return driverName;
	}

	/**
	 * @param driverName
	 *            the driverName to set
	 */
	public void setDriverName(final String driverName) {

		this.driverName = driverName;
	}

	/**
	 * @return the connectionName
	 */
	public String getConnectionName() {

		return connectionName;
	}

	/**
	 * @param connectionName
	 *            the connectionName to set
	 */
	public void setConnectionName(final String connectionName) {

		this.connectionName = connectionName;
	}

	/**
	 * Initializes the database.
	 * 
	 * @param driverName
	 * @param connectionName
	 * @throws BookException
	 */
	private void initializeDB(final String driverName,
			final String connectionName) throws BookException {

		// register driver - not necessary in java 6
		try {
			Class.forName(driverName);
		}
		catch (ClassNotFoundException e) {
			final String error = "Database driver class not found for driver "
					+ driverName;
			logger.error(error);
			throw new BookException(error, e);
		}

		try {
			connection = DriverManager.getConnection(connectionName);
		}
		catch (SQLException e) {
			final String error = "Unable to create database connection "
					+ connectionName;
			logger.error(error);
			throw new BookException(error, e);
		}
	}
}
