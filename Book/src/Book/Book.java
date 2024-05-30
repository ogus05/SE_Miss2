package Book;

//Book의 정보들을 멤버변수로 갖는 class.
public class Book {
	
	//Book이 생성된 이후 내부 변수 변경 불가능하도록 private 선언 후 getter 구현.
	private int id;
	private String title;
	private String author;
	private int publicDate;
	
	public Book(int in_id, String in_title, String in_author, int in_publicDate) {
		id = in_id;
		title = in_title;
		author = in_author;
		publicDate = in_publicDate;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public int getPublicDate() {
		return publicDate;
	}
}
