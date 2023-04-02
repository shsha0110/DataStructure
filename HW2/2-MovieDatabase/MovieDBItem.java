public class MovieDBItem implements Comparable<MovieDBItem> {

    private final String genre;
    private final String title;

    public MovieDBItem(String genre, String title) {
        if (genre == null) throw new NullPointerException("genre");
        if (title == null) throw new NullPointerException("title");

        this.genre = genre;
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int compareTo(MovieDBItem other) {
        // TODO

        // Criterion1 ) Genre
        String genre1 = this.genre;
        String genre2 = other.genre;
        if (!genre1.equals(genre2)) {
            return genre1.compareTo(genre2);
        }
        // Criterion2 ) Title
        String title1 = this.title;
        String title2 = other.title;
        return title1.compareTo(title2);

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MovieDBItem other = (MovieDBItem) obj;
        if (genre == null) {
            if (other.genre != null)
                return false;
        } else if (!genre.equals(other.genre))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((genre == null) ? 0 : genre.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    public boolean containsTerm(String term) {

        String upperTitle = title.toUpperCase();
        String upperTerm = term.toUpperCase();

        return upperTitle.contains(upperTerm);
//
//        if (upperTitle.length() < upperTerm.length()) { return false; }
//
//        for (int i = 0; i < title.length() - term.length(); i++) {
//
//            String subString = upperTitle.substring(i, i + term.length());
//
//            if (subString.equals(upperTerm)) {
//                return true;
//            }
//        }
//
//        return false;
    }
}
