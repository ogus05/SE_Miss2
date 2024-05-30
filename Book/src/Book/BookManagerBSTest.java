package Book;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.LinkedList;
import java.util.List;

//BookManagerBS를 test하기 위한 class.
class BookManagerBSTest {
	//각 test전 bookManagerBS에 입력되는 book의 개수.
	final int NUMB_OF_TEST_CASES = 10000;
	
	//Test하기 위한 BookManagerBS instance.
	BookManagerBS bookManager;
	
	//Test 전 bookManager에 추가되는 book 중 하나.
	Book bookInBookManager;
	
	//Test 전 bookManager에 추가되지 않은 book 하나.
	Book bookNotInBookManager;
	
	@BeforeEach
	void setUp() throws Exception {
		bookManager = new BookManagerBS();
		
		//bookInBookManager 및 bookNotInBookManager의 ID를 난수로 지정.
		int ID_bookInBookManager = (int) (System.currentTimeMillis() % NUMB_OF_TEST_CASES);
		int ID_bookNotInBookManager = (int) (System.currentTimeMillis() % NUMB_OF_TEST_CASES);
		
		//bookInBookManager != bookNotInBookManager를 위해 loop문을 활용.
		while(ID_bookInBookManager == ID_bookNotInBookManager) {
			ID_bookNotInBookManager = (int) (System.currentTimeMillis() % NUMB_OF_TEST_CASES);
		}
		
		System.out.printf("ID of a Book In BookManager = %d\n", ID_bookInBookManager);
		System.out.printf("ID of a Book Not In BookManager = %d\n", ID_bookNotInBookManager);
		
		//BookManager에 추가되어야 할 book의 ID를 담고 있는 list.
		List<Integer> remainIDList = new LinkedList<Integer>();
		for(int i = 0; i < NUMB_OF_TEST_CASES; i++) {
			remainIDList.add(i);
		}
		
		//Test 전 setup 과정. NUMB_OF_TEST_CASES의 개수 만큼 bookManager에 book추가.
		//Test를 위해 입력되는 id의 순서를 랜덤하게 설정.
		while(remainIDList.size() != 0) {
			int remainIDListIdxInCurrentLoop = (int)(System.currentTimeMillis() % remainIDList.size());
			int bookID = remainIDList.get(remainIDListIdxInCurrentLoop);
			String bookTitle = Integer.toString(bookID) + "번째 책";
			String bookAuthor = Integer.toString(bookID) + "번째 책 저자";
			//Book의 publicDate는 bookID에 2024를 더한 값.
			int bookPublicDate = bookID + 2024;
			
			Book bookInCurrentLoop = new Book(bookID, bookTitle, bookAuthor, bookPublicDate);
			if(bookID != ID_bookNotInBookManager) {
				bookManager.AddBook(bookInCurrentLoop.getId(), bookInCurrentLoop.getTitle(), bookInCurrentLoop.getAuthor(), bookInCurrentLoop.getPublicDate());
				if(bookID == ID_bookInBookManager) {
					bookInBookManager = bookInCurrentLoop;
				}
			} else {
				bookNotInBookManager = bookInCurrentLoop;
			}
			
			remainIDList.remove(remainIDListIdxInCurrentLoop);
		}
	}

	@Test
	void testAddBooks() {
		try {
			//bookNotInBookManager를 bookManager에 추가. -> 정상적인 return.
			Book retBook = bookManager.AddBook(bookNotInBookManager.getId(), bookNotInBookManager.getTitle(), bookNotInBookManager.getAuthor(), bookNotInBookManager.getPublicDate());
			assertEquals(bookNotInBookManager.getId(), retBook.getId(), "bookNotInBookManager, retBook ID is not equal");
			System.out.printf("{id: '%d', 제목: '%s', 저자 : '%s', 출판년도 : '%d'}도서가 추가되었습니다.\n", retBook.getId(), retBook.getTitle(), retBook.getAuthor(), retBook.getPublicDate());		

			//bookInBookManager를 bookManager에 추가. -> throws exception.
			BookManagerException ex = assertThrows(BookManagerException.class, () -> bookManager.AddBook(bookInBookManager.getId(), bookInBookManager.getTitle(), bookInBookManager.getAuthor(), bookInBookManager.getPublicDate()));
			System.out.println(ex.getMessage());
		} catch(BookManagerException exc){
		}
	}

	@Test
	void testSearchBooks() {
		try {
			//bookInBookManager를 bookManager에서 탐색. -> 정상적인 return.
			Book retBook = bookManager.SearchBook(bookInBookManager.getId());
			assertEquals(bookInBookManager.getId(), retBook.getId(), "testCase, retBook ID is not equal");
			System.out.printf("검색결과:\n");
			System.out.printf("{id: '%d', 제목: '%s', 저자 : '%s', 출판년도 : '%d'}\n", retBook.getId(), retBook.getTitle(), retBook.getAuthor(), retBook.getPublicDate());			
			
			//bookNotInBookManager를 bookManager에서 탐색. -> throws exception.
			BookManagerException ex = assertThrows(BookManagerException.class, () -> bookManager.SearchBook(bookNotInBookManager.getId()));
			System.out.println(ex.getMessage());

		} catch(BookManagerException exc){
		}
		
	}

	@Test
	void testRemoveBooks() {
		try {
			//bookInBookManager를 bookManager에서 제거. -> 정상적인 return.
			Book retBook = bookManager.RemoveBook(bookInBookManager.getId());
			assertEquals(bookInBookManager.getId(), retBook.getId(), "testCase, retBook ID is not equal");
			System.out.printf("{id: '%d', 제목: '%s', 저자 : '%s', 출판년도 : '%d'}도서가 삭제되었습니다.\n", retBook.getId(), retBook.getTitle(), retBook.getAuthor(), retBook.getPublicDate());		
			
			
			//bookNotInBookManager를 bookManager에서 제거. -> throws exception.
			BookManagerException ex = assertThrows(BookManagerException.class, () -> bookManager.RemoveBook(bookNotInBookManager.getId()));
			System.out.println(ex.getMessage());

		} catch(BookManagerException exc){
		}
		
	}

}
