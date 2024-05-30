package Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//BookManager들의 performance를 test하기 위한 class.
class BookManagerPerformanceTest {
	//각 test전 bookManager들에 입력되는 book의 개수.
	final int NUMB_OF_TEST_CASES = 10000;
	
	//각 test에서 add, remove, search되는 book의 개수.
	//각 test는 NUMB_OF_SEARCH_CASES번 만큼 iteration.
	final int NUMB_OF_SEARCH_CASES = 100;
		
	//Test하기 위한 BookManager instance.
	BookManager bookManager;
	
	//Test하기 위한 BookManagerBS instance.
	BookManagerBS bookManagerBS;
	
	//Test 전 bookManager에 추가되는 book들 중 NUMB_OF_SEARCH_CASES개.
	List<Book> bookInBookManagerList;
	
	//Test 전 bookManager에 추가되지 않은 book의  NUMB_OF_SEARCH_CASES개.
	List<Book> bookNotInBookManagerList;
	
	@BeforeEach
	void setUp() throws Exception {
		
		bookManager = new BookManager();
		bookManagerBS = new BookManagerBS();
		
		bookInBookManagerList = new LinkedList<Book>();
		bookNotInBookManagerList = new LinkedList<Book>();

		//BookManager에 입력될 test할 ID를 Add되는 순서를 통해 지정.
		int insertedSeq_bookInBookManager = (int) (System.currentTimeMillis() % NUMB_OF_TEST_CASES) / NUMB_OF_SEARCH_CASES;
		//BookManager에 입력되지 않을 test할 ID를 Add되는 순서를 통해 지정.
		int insertedSeq_ID_bookNotInBookManager = (int) (System.currentTimeMillis() % NUMB_OF_TEST_CASES) / NUMB_OF_SEARCH_CASES;
		
		//insertedSeq_bookInBookManager != insertedSeq_ID_bookNotInBookManager를 위해 loop문을 활용.
		while(insertedSeq_bookInBookManager == insertedSeq_ID_bookNotInBookManager) {
			insertedSeq_ID_bookNotInBookManager = (int) (System.currentTimeMillis() % NUMB_OF_TEST_CASES) / NUMB_OF_SEARCH_CASES;
		}
		
		System.out.printf("Inserted Sequnece of Books In BookManager = %d\n", insertedSeq_bookInBookManager);
		System.out.printf("Inserted Sequnece  of Books Not In BookManager = %d\n", insertedSeq_ID_bookNotInBookManager);
		
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
			if((remainIDListIdxInCurrentLoop / NUMB_OF_SEARCH_CASES) != insertedSeq_ID_bookNotInBookManager) {
				bookManager.AddBook(bookInCurrentLoop.getId(), bookInCurrentLoop.getTitle(), bookInCurrentLoop.getAuthor(), bookInCurrentLoop.getPublicDate());
				bookManagerBS.AddBook(bookInCurrentLoop.getId(), bookInCurrentLoop.getTitle(), bookInCurrentLoop.getAuthor(), bookInCurrentLoop.getPublicDate());
				if((remainIDListIdxInCurrentLoop / NUMB_OF_SEARCH_CASES) == insertedSeq_bookInBookManager) {
					bookInBookManagerList.add(bookInCurrentLoop);
				}
			} else {
				bookNotInBookManagerList.add(bookInCurrentLoop);
			}
			
			remainIDList.remove(remainIDListIdxInCurrentLoop);
		}
	}

	@Test
	void testAddBooks() {
		try {
			Iterator<Book> itr_bookNotInBookManager = bookNotInBookManagerList.iterator();
			
			//Linear search book manager class의 add method iteration 전 시작 시간. 시각은 nano초로 표현.
			long startLinearAddTimeInNano= System.nanoTime();
			while(itr_bookNotInBookManager.hasNext()) {
				Book bookToAdd = itr_bookNotInBookManager.next();
				bookManager.AddBook(bookToAdd.getId(), bookToAdd.getTitle(), bookToAdd.getAuthor(), bookToAdd.getPublicDate());
			}
			//Linear search book manager class의 add method iteration의 실행 시간 출력.
			System.out.printf("Add books in linear search : %d(ns)\n", (System.nanoTime() - startLinearAddTimeInNano));

			itr_bookNotInBookManager = bookNotInBookManagerList.iterator();
			
			//Binary search book manager class의 add method iteration 전 시작 시간. 시각은 nano초로 표현.
			long startBinaryAddTimeInNano = System.nanoTime();
			while(itr_bookNotInBookManager.hasNext()) {
				Book bookToAdd = itr_bookNotInBookManager.next();
				bookManagerBS.AddBook(bookToAdd.getId(), bookToAdd.getTitle(), bookToAdd.getAuthor(), bookToAdd.getPublicDate());
			}
			//Binary search book manager class의 add method iteration의 실행 시간 출력.
			System.out.printf("Add books in binary search : %d(ns)\n", (System.nanoTime() - startBinaryAddTimeInNano));

		} catch(BookManagerException exc){
		}
	}

	@Test
	void testSearchBooks() {
		try {
			Iterator<Book> itr_bookInBookManager = bookInBookManagerList.iterator();
			
			//Linear search book manager class의 search method iteration 전 시작 시간. 시각은 nano초로 표현.
			long startLinearSearchTimeInNano= System.nanoTime();
			while(itr_bookInBookManager.hasNext()) {
				Book bookToSearch = itr_bookInBookManager.next();
				bookManager.SearchBook(bookToSearch.getId());
			}
			//Linear search book manager class의 search method iteration의 실행 시간 출력.
			System.out.printf("Search books in linear search : %d(ns)\n", (System.nanoTime() - startLinearSearchTimeInNano));

			itr_bookInBookManager = bookInBookManagerList.iterator();
			
			//Binary search book manager class의 search method iteration 전 시작 시간. 시각은 nano초로 표현.
			long startBinarySearchTimeInNano= System.nanoTime();
			while(itr_bookInBookManager.hasNext()) {
				Book bookToSearch = itr_bookInBookManager.next();
				bookManagerBS.SearchBook(bookToSearch.getId());
			}
			//Binary search book manager class의 search method iteration의 실행 시간 출력.
			System.out.printf("Search books in binary search : %d(ns)\n", (System.nanoTime() - startBinarySearchTimeInNano));

		} catch(BookManagerException exc){
		}
		
	}

	@Test
	void testRemoveBooks() {
		try {
			Iterator<Book> itr_bookInBookManager = bookInBookManagerList.iterator();
			
			//Linear search book manager class의 remove method iteration 전 시작 시간. 시각은 nano초로 표현.
			long startLinearRemoveTimeInNano= System.nanoTime();
			while(itr_bookInBookManager.hasNext()) {
				Book bookToRemove = itr_bookInBookManager.next();
				bookManager.RemoveBook(bookToRemove.getId());
			}
			//Linear search book manager class의 remove method iteration의 실행 시간 출력.
			System.out.printf("Remove books in linear search : %d(ns)\n", (System.nanoTime() - startLinearRemoveTimeInNano));

			itr_bookInBookManager = bookInBookManagerList.iterator();
			
			//Binary search book manager class의 remove method iteration 전 시작 시간. 시각은 nano초로 표현.
			long startBinaryRemoveTimeInNano= System.nanoTime();
			while(itr_bookInBookManager.hasNext()) {
				Book bookToRemove = itr_bookInBookManager.next();
				bookManagerBS.RemoveBook(bookToRemove.getId());
			}
			//Binary search book manager class의 remove method iteration의 실행 시간 출력.
			System.out.printf("Remove books in binary search : %d(ns)\n", (System.nanoTime() - startBinaryRemoveTimeInNano));

		} catch(BookManagerException exc){
		}
		
	}

}
