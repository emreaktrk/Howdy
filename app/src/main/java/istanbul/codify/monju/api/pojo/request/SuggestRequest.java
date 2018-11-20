package istanbul.codify.monju.api.pojo.request;


public final class SuggestRequest {

    public String token;
    public String word;
    public long catID;

    public SuggestRequest(String word, long categoryId) {
        this.word = word;
        this.catID = categoryId;
    }

    public SuggestRequest(String word) {
        this.word = word;
    }
}
