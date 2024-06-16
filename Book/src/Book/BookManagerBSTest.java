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
	
	//Test대상인 BookManagerBS instance.
	BookManagerBase bookManager;
	
	//Test 전 bookManager에 추가되는 book instance.
	//Search, Remove에서 정상적인 동작, Add에서 오류를 tset하기 위한 book instance.
	Book bookInBookManager;
	
	//Test 전 bookManager에 추가되지 않는 book instance.
	//Add에서 정상적인 동작, Search, Remove에서 오류를 test하기 위한 book instance.
	Book bookNotInBookManager;
	
	//출력 값. random number [0, NUMB_OF_TSET_CASES).
	int getRandomNumberForBookID() {
		return (int) (System.currentTimeMillis() % NUMB_OF_TEST_CASES);
	}
	
	//입력 값. book id.
	//출력 값. 입력된 book id를 갖는 book instance.
	//동작 요약. Test를 위한 dummy book을 생성 후 반환.
	Book makeDummyBook(int bookID) {
		String bookTitle = Integer.toString(bookID) + "번째 책";
		String bookAuthor = Integer.toString(bookID) + "번째 책 저자";
		//Book의 publicDate는 bookID에 2024를 더한 값.
		int bookPublicDate = bookID + 2024;
		return new Book(bookID, bookTitle, bookAuthor, bookPublicDate);
	}
	
	//동작 요약.
	//Random하게 NUMB_OF_TEST_CASES - 1개만큼 bookManager에 book instance를 add.
	//Random한 값 중 2가지를 선택하여 list에 add된 book instance 중 하나는 bookInBookManager가 가리키도록 한다.
	//list에 add되지 않은 book instance는 bookNotInBookManager가 가리키도록 한다.
	void addItemToListBeforeTest() throws Exception {
		//bookInBookManager 및 bookNotInBookManager의 ID를 random하게 설정.
		int ID_bookInBookManager = getRandomNumberForBookID();
		int ID_bookNotInBookManager = getRandomNumberForBookID();
		while(ID_bookInBookManager == ID_bookNotInBookManager) {
			ID_bookNotInBookManager = getRandomNumberForBookID();
		}
		
		System.out.printf("ID of a Book In BookManager = %d\n", ID_bookInBookManager);
		System.out.printf("ID of a Book Not In BookManager = %d\n", ID_bookNotInBookManager);
		
		//BookManager에 추가되어야 할 book의 ID를 담고 있는 list.
		//BookManager에 저장된 book instance가 random한 sequence를 갖게하기 위해 활용된다.
		List<Integer> remainIDList = new LinkedList<Integer>();
		for(int i = 0; i < NUMB_OF_TEST_CASES; i++) {
			remainIDList.add(i);
		}
		
		
		while(remainIDList.size() != 0) {
			//remainIDList의 random한 index가 가리키는 book ID를 bookManager에 add한다.
			int remainIDListIdxInCurrentLoop = (int)(System.currentTimeMillis() % remainIDList.size());
			
			Book bookInCurrentLoop = makeDummyBook(remainIDList.get(remainIDListIdxInCurrentLoop));
			
			if(bookInCurrentLoop.getId() != ID_bookNotInBookManager) {
				bookManager.AddBook(bookInCurrentLoop.getId(), bookInCurrentLoop.getTitle(), bookInCurrentLoop.getAuthor(), bookInCurrentLoop.getPublicDate());
				if(bookInCurrentLoop.getId() == ID_bookInBookManager) {
					bookInBookManager = bookInCurrentLoop;
				}
			} else {
				bookNotInBookManager = bookInCurrentLoop;
			}
			
			remainIDList.remove(remainIDListIdxInCurrentLoop);
		}
	}
	
	@BeforeEach
	void setUp() throws Exception {
		bookManager = new BookManagerBS();
		addItemToListBeforeTest();
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
