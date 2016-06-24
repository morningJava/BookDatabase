package view;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.BookDO;
import model.BookException;
import model.DAO;

public class BookApplication {
	private static final Logger logger = LogManager
			.getLogger(BookApplication.class);

	public static void main(String[] args) throws BookException {
		final DAO bookDao = new BookDAOImpl();

		logger.info("Adding test Books");
		addTestBooks(bookDao);

		listAllBooks(bookDao);

		BookDO bookDo = new BookDO(00001,"Game of Thrones", null, null, null);
		
		logger.info("Updating " + bookDo);
		bookDo.setAuthor("George RR Martin");
		bookDo.setDescription("The best characters die");
		bookDo.setPages(1000);
		logger.info("Finding book by " + bookDo.getAuthor());

		try {
			bookDao.find(bookDo);
		} catch (BookException e) {
			logger.warn("Error retriving book " + bookDo, e);
		}

	

		try {
			bookDao.update(bookDo);
		} catch (BookException e) {
			logger.warn("Error updating Test " + bookDo, e);
		}

		listAllBooks(bookDao);

		

		try {
			bookDao.add(bookDo);
		} catch (BookException e) {
			logger.warn("This is the expected exception", e);
		}

		logger.info("Removing book " + bookDo);
		try {
			bookDao.remove(bookDo);
		} catch (BookException e) {
			logger.warn("Error deleting book " + bookDo, e);
		}

		List<BookDO> books = listAllBooks(bookDao);

		logger.info("Removing all Books");
		for (BookDO book : books) {
			try {
				bookDao.remove(book);
			} catch (BookException e) {
				logger.warn("Error removing Book " + book);
			}
		}
	}

	private static List<BookDO> listAllBooks(final DAO bookDao) {
		List<BookDO> Books = null;

		logger.info("Finding all books");
		try {
			Books = bookDao.findAll();
		} catch (BookException e) {
			logger.warn("Error finding all books", e);
		}

		if (Books == null || Books.isEmpty()) {
			logger.info("No Books in list");
		} else {
			logger.info("Listing all Books");
			for (final BookDO Book : Books) {
				logger.info(Book);
			}
		}
		logger.info("End of list");

		return Books;
	}

	private static void addTestBooks(final DAO bookDao) {
		BookDO bookDo = new BookDO(00001,"Game of Thrones", "George RR Martain",
				"The best characters die", 1000);
		addBook(bookDao, bookDo);

		bookDo = new BookDO(00002,"CO 217 Advanced Java Programming", "Daniel Lang",
				"GRCC CIS department", 178);
		addBook(bookDao, bookDo);

		bookDo = new BookDO(00003,"Hacking: The Art of Exploitation 2nd Edition",
				"Jon Erickson", "hack all the things", 471);
		addBook(bookDao, bookDo);

		bookDo = new BookDO(00004,"A Journal of the Plague Year", "Daniel Defoe",
				"Bring out your dead!", 186);
		addBook(bookDao, bookDo);

	}

	private static void addBook(final DAO bookDao, final BookDO bookDo) {
		try {
			bookDao.add(bookDo);
		} catch (BookException e) {
			logger.warn("Error adding book " + bookDo, e);
		}
	}
}
