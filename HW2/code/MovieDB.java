import java.util.Iterator;
import java.util.NoSuchElementException;

public class MovieDB {

	MyLinkedList<MovieList> MovieLists;

	Node<MovieList> head = new Node<>(null);

    public MovieDB() {
        // FIXME implement this
    	this.MovieLists = new MyLinkedList<>();
	}

    public void insert(MovieDBItem item) {
        // FIXME implement this
        // Insert the given item to the MovieDB.
		// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.

		String genre = item.getGenre();
		String title = item.getTitle();

		Node<MovieList> prevNode = null;
		Node<MovieList> currNode = head;

		while (currNode.getNext() != null) {

			prevNode = currNode;
			currNode = currNode.getNext();

			String currGenre = currNode.getItem().Genre;

			if (genre.equals(currGenre)) {

				currNode.getItem().add(title);

				return;

			}

			if (genre.compareTo(currGenre) < 0) {

				MovieList newMovieList = new MovieList(item);

				prevNode.insertNext(newMovieList);

				return;

			}

		}

		MovieList newMovieList = new MovieList(item);

		currNode.insertNext(newMovieList);

		return;

		// This code should be removed before submitting your work.
		//System.err.printf("[trace] MovieDB: INSERT [%s] [%s]\n", item.getGenre(), item.getTitle());
    }

    public void delete(MovieDBItem item) {
        // FIXME implement this
        // Remove the given item from the MovieDB.
    	// Printing functionality is provided for the sake of debugging.

		String genre = item.getGenre();
		String title = item.getTitle();

		Node<MovieList> prevNode = null;
		Node<MovieList> currNode = head;

		while (currNode.getNext() != null) {

			prevNode = currNode;
			currNode = currNode.getNext();

			MovieList currMovieList = currNode.getItem();
			String currGenre = currMovieList.Genre;

			if (genre.equals(currGenre)) {

				Iterator<String> movieListIterator = currMovieList.iterator();

				while (movieListIterator.hasNext()) {

					String currTitle = movieListIterator.next();

					if (title.equals(currTitle)) {

						movieListIterator.remove();

						return;
					}

				}

				return;
			}

		}

		// This code should be removed before submitting your work.
        // System.err.printf("[trace] MovieDB: DELETE [%s] [%s]\n", item.getGenre(), item.getTitle());
    }

    public MyLinkedList<MovieDBItem> search(String term) {
        // FIXME implement this
        // Search the given term from the MovieDB.
        // You should return a linked list of MovieDBItem.
        // The search command is handled at SearchCmd class.
    	
    	// Printing search results is the responsibility of SearchCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.

		MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();

		Node<MovieList> prevNode = null;
		Node<MovieList> currNode = head;

		while (currNode.getNext() != null) {

			prevNode = currNode;
			currNode = currNode.getNext();

			MovieList currMovieList = currNode.getItem();
			String currGenre = currMovieList.Genre;


			Iterator<String> movieListIterator = currMovieList.iterator();

			while (movieListIterator.hasNext()) {

				String currTitle = movieListIterator.next();

				MovieDBItem item = new MovieDBItem(currGenre, currTitle);

				if (item.containsTerm(term)) {
					results.add(item);
				}

			}

		}

		return results;

		// This code should be removed before submitting your work.
    	// System.err.printf("[trace] MovieDB: SEARCH [%s]\n", term);
    }
    
    public MyLinkedList<MovieDBItem> items() {
        // FIXME implement this
        // Search the given term from the MovieDatabase.
        // You should return a linked list of QueryResult.
        // The print command is handled at PrintCmd class.

    	// Printing movie items is the responsibility of PrintCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.

		MyLinkedList<MovieDBItem> Items = new MyLinkedList<MovieDBItem>();

		Node<MovieList> prevNode = null;
		Node<MovieList> currNode = head;

		MovieList currMovieList;
		String currGenre;

		while (currNode.getNext() != null) {

			prevNode = currNode;
			currNode = currNode.getNext();

			currMovieList = currNode.getItem();
			currGenre = currMovieList.Genre;

			Iterator<String> titles = currMovieList.iterator();

			while (titles.hasNext()) {

				String currTitle = titles.next();

				MovieDBItem Item = new MovieDBItem(currGenre, currTitle);

				Items.add(Item);

			}

		}

		return Items;

		// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.
        //System.err.printf("[trace] MovieDB: ITEMS\n");
    }
}

class Genre extends Node<String> implements Comparable<Genre> {

	private int numOfMoviesInGenre;
	public Genre(String name) {
		super(name);
		this.numOfMoviesInGenre = 0;
	}
	
	@Override
	public int compareTo(Genre o) {

		String genreName1 = this.getItem();
		String genreName2 = o.getItem();

		return genreName1.compareTo(genreName2);

	}

	// XXX
	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	// TODO
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (obj instanceof Genre) {
			Genre genre = (Genre) obj;
			return this.getItem().equals(genre.getItem());
		}

		return false;

	}
}

class MovieList implements ListInterface<String> {

	// TODO
	Node<String> head;

	String Genre;

	int numItems = 0;

	public MovieList() { head = new Node<>(null); }

	public MovieList(MovieDBItem item) {

		this();

		Genre = item.getGenre();

		this.add(item.getTitle());

		numItems++;

	}

	@Override
	public final Iterator<String> iterator() { return new MovieListIterator(this); }

	@Override
	public boolean isEmpty() {
		return head.getNext() == null;
	}

	@Override
	public int size() {
		return numItems;
	}

	@Override
	public void add(String title) {

		Node<String> prevNode = null;
		Node<String> currNode = head;

		String currTitle;

		while (currNode.getNext() != null) {

			prevNode = currNode;
			currNode = currNode.getNext();

			currTitle = currNode.getItem();

			if (title.equals(currTitle)) { return; }

			if (title.compareTo(currTitle) < 0) {

				prevNode.insertNext(title);

				numItems++;

				return;
			}

		}

		currNode.insertNext(title);

		numItems++;

		return;

	}

	@Override
	public String first() {
		return head.getNext().getItem();
	}

	@Override
	public void removeAll() { head.setNext(null); }
}

class MovieListIterator implements Iterator<String> {

	MovieList movieList;
	private Node<String> prev;
	private Node<String> curr;

	public MovieListIterator(MovieList movieList) {
		this.movieList = movieList;
		prev = null;
		curr = movieList.head;
	}

	@Override
	public boolean hasNext() {
		return curr.getNext() != null;
	}

	@Override
	public String next() {

		if (curr == null) { throw new NoSuchElementException(); }

		prev = curr;
		curr = curr.getNext();

		return curr.getItem();
	}

	@Override
	public void remove() {

		if (prev == null)
			throw new IllegalStateException("next() should be called first");
		if (curr == null)
			throw new NoSuchElementException();

		prev.removeNext();
		movieList.numItems -= 1;
		curr = prev;
		prev = null;
	}

}
