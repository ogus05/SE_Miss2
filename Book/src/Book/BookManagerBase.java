package Book;
import java.util.LinkedList;
import java.util.List;

//BookManager의 add, search, remove가 구현된 class.
//상속을 위해 추상 method인 InnerSearch의 구현 필요.
abstract public class BookManagerBase {
	//현재 저장된 책들의 정보.
	List<Book> bookList;
	
	public BookManagerBase() {
		bookList = new LinkedList<Book>();
	}

	//InnerSearch. bookList에서 in_id와 같은 id를 갖는 Book instance를 찾아 반환.
	//입력 값. 찾을 책의 id.
	//반환 값. 찾은 책의 Book instance or 책을 찾지 못하면 null.
	abstract protected Book InnerSearch(int in_id);

	//AddBook. Book의 정보를 받아 book instance 생성 및 bookList에 추가.
	//입력 값. bookList에 추가 할 Book의 정보.
	//반환 값. 추가된 책의 book instance or 같은 id의 책이 이미 존재한다면 throws exception.
	public Book AddBook(int in_id, String in_title, String in_author, int in_publicDate) throws BookManagerException {
		if(InnerSearch(in_id) != null) {
			throw new BookManagerException("해당 ID(" + Integer.toString(in_id) + ")는 이미 존재합니다.\n");
		} else {
			Book book = new Book(in_id, in_title, in_author, in_publicDate);
			bookList.add(book);
			return book;
		}
	}
	
	//SearchBook. bookList에서 in_id와 같은 id를 갖는 Book instance를 찾아 반환.
	//입력 값. 찾을 책의 id.
	//반환 값. 찾은 책의 Book instance or 책을 찾지 못하면 throws exception.
	public Book SearchBook(int in_id) throws BookManagerException{
		Book book = InnerSearch(in_id);
		if(book != null) {
			return book;
		} else {
			throw new BookManagerException("검색된 도서가 없습니다.\n");
		}

	}
	
	//RemoveBook. bookList에서 in_id와 같은 id를 갖는 Book instance를 찾아 제거.
	//입력 값. 제거할 책의 id.
	//반환 값. 제거한 책의 Book instance or 제거할 책을 찾지 못하면 throws exception.
	public Book RemoveBook(int in_id) throws BookManagerException{
		Book book = InnerSearch(in_id);
		if(book != null) {	
			bookList.remove(book);
			return book;
		} else {
			throw new BookManagerException("해당 ID(" + Integer.toString(in_id) + ")의 도서를 찾을 수 없습니다.\n");
		}
	}
}
