package Book;
import java.util.*;


//BookManagerBase의 InnerSearch를 lenearSearch로 구현한 class.
public class BookManager extends BookManagerBase {
	
	public BookManager() {
		super();
	}

	
	@Override
	protected Book InnerSearch(int in_id) {
		Iterator<Book> itr_book = bookList.iterator();
		
		//LinearSearch.
		while(itr_book.hasNext()) {
			Book bookInCurrentLoop = itr_book.next();
			if(bookInCurrentLoop.getId() == in_id) {
				return bookInCurrentLoop;
			}
		}
		return null;
	}

}
