package Book;
import java.util.*;


//BookManagerBase의 InnerSearch를 lenearSearch로 구현한 class.
public class BookManager extends BookManagerBase {
	
	public BookManager() {
		super();
	}

	
	//입력값. book id.
	//출력값. 입력 id를 갖는 book or null.
	//동작 요약. Linear Search를 통해 입력값의 id를 갖는 book을 찾아 반환.
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
