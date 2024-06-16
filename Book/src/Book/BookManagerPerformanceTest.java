package Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//BookManager들의 performance를 test하기 위한 class.
//Add, Remove, Search의 performance를 test한다.


class BookManagerPerformanceTest {
	//각 test전 bookManager들에 입력되는 book의 개수.
	final int NUMB_OF_TEST_CASES = 10000;
	
	//각 test에서 add, remove, search되는 book의 개수.
	final int NUMB_OF_SEARCH_CASES = 100;
		
	//Test대상인 BookManagerBS instance.
	BookManagerBase bookManager;
	
	//Test대상인 BookManagerBS instance.
	BookManagerBase bookManagerBS;
	
	//Test 전 bookManager에 추가되는 book instance의 NUMB_OF_SEARCH_CASES개.
	//Search, Remove에서 정상적인 동작, Add에서 오류를 tset하기 위한 book instance list.
	List<Book> bookInBookManagerList;
	
	//Test 전 bookManager에 추가되지 않은 book instance의 NUMB_OF_SEARCH_CASES개.
	//Add에서 정상적인 동작, Search, Remove에서 오류를 test하기 위한 book instance list.
	List<Book> bookNotInBookManagerList;
	
	//출력 값. random number [0, (NUMB_OF_TSET_CASES / NUMB_OF_SEARCH_CASES)).
	int getRandomNumberForBookIDCluster() {
		return (int) (System.currentTimeMillis() % NUMB_OF_TEST_CASES) / NUMB_OF_SEARCH_CASES;
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
	//Random하게 NUMB_OF_TEST_CASES - NUMB_OF_SEARCH_CASES개만큼 bookManager에 book instance를 add.
	//Random한 값 중 (2 * NUMB_OF_SEARCH_CASES)가지를 선택하여 list에 add된 book instance 중 NUMB_OF_SEARCH_CASES개는 bookInBookManager가 가리키도록 한다.
	//list에 add되지 않은 book instance 중 NUMB_OF_SEARCH_CASES개는 bookNotInBookManager가 가리키도록 한다.
	void addItemsToListBeforeTest() throws Exception {
		//bookInBookManager 및 bookNotInBookManager의 ID_CLUSTER를 random하게 설정.
		//[ID_Cluster * NUMB_OF_SEARCH_CASES, (ID_Cluster + 1) * NUMB_OF_SEARCH_CASES)의 범위에 포함되는 ID가 bookInBookManager 및 bookNotInBookManager의 list에 add된다.
		int ID_CLUSTER_bookInBookManager = getRandomNumberForBookIDCluster();
		int ID_CLUSTER_bookNotInBookManager = getRandomNumberForBookIDCluster();
		while(ID_CLUSTER_bookInBookManager == ID_CLUSTER_bookNotInBookManager) {
			ID_CLUSTER_bookNotInBookManager = getRandomNumberForBookIDCluster();
		}
		
		System.out.printf("Inserted Sequnece of Books In BookManager = %d - %d\n", ID_CLUSTER_bookInBookManager * NUMB_OF_SEARCH_CASES, (ID_CLUSTER_bookInBookManager + 1) * NUMB_OF_SEARCH_CASES - 1);
		System.out.printf("Inserted Sequnece  of Books Not In BookManager = %d - %d\n", ID_CLUSTER_bookNotInBookManager * NUMB_OF_SEARCH_CASES, (ID_CLUSTER_bookNotInBookManager + 1) * NUMB_OF_SEARCH_CASES - 1);
		
		//BookManager에 추가되어야 할 book의 ID를 담고 있는 list.
		//BookManager에 저장된 book instance가 random한 sequence를 갖게하기 위해 활용된다.
		List<Integer> remainIDList = new LinkedList<Integer>();
		for(int i = 0; i < NUMB_OF_TEST_CASES; i++) {
			remainIDList.add(i);
		}
		
		while(remainIDList.size() != 0) {
			int remainIDListIdxInCurrentLoop = (int)(System.currentTimeMillis() % remainIDList.size());
			
			Book bookInCurrentLoop = makeDummyBook(remainIDList.get(remainIDListIdxInCurrentLoop));
			
			if((bookInCurrentLoop.getId() / NUMB_OF_SEARCH_CASES) != ID_CLUSTER_bookNotInBookManager) {
				bookManager.AddBook(bookInCurrentLoop.getId(), bookInCurrentLoop.getTitle(), bookInCurrentLoop.getAuthor(), bookInCurrentLoop.getPublicDate());
				bookManagerBS.AddBook(bookInCurrentLoop.getId(), bookInCurrentLoop.getTitle(), bookInCurrentLoop.getAuthor(), bookInCurrentLoop.getPublicDate());
				if((bookInCurrentLoop.getId() / NUMB_OF_SEARCH_CASES) == ID_CLUSTER_bookInBookManager) {
					bookInBookManagerList.add(bookInCurrentLoop);
				}
			} else {
				bookNotInBookManagerList.add(bookInCurrentLoop);
			}
			
			//추가된 book ID를 제거.
			remainIDList.remove(remainIDListIdxInCurrentLoop);
		}
	}
	
	@BeforeEach
	void setUp() throws Exception {
		bookManager = new BookManager();
		bookManagerBS = new BookManagerBS();
		
		bookInBookManagerList = new LinkedList<Book>();
		bookNotInBookManagerList = new LinkedList<Book>();
		
		addItemsToListBeforeTest();
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
