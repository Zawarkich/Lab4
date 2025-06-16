package wikisearch.wiki_search.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import wikisearch.wiki_search.entity.*;
import wikisearch.wiki_search.repository.*;
import java.util.List;

@Service
public class WikiService {
    private final SearchHistoryRepository historyRepo;
    private final WikiArticleRepository articleRepo;
    private final RestTemplate restTemplate;

    public WikiService(SearchHistoryRepository historyRepo, 
                     WikiArticleRepository articleRepo) {
        this.historyRepo = historyRepo;
        this.articleRepo = articleRepo;
        this.restTemplate = new RestTemplate();
    }
    
    @SuppressWarnings("null")
    public WikiArticle search(String term) {
        String url = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&exintro=true&explaintext=true&titles=" + term;
        String response = restTemplate.getForObject(url, String.class);
        String extract;
        System.out.println("started wiki req");
        if (response.contains("extract")) {
            extract = response.split("\"extract\":\"")[1].split("\"")[0];
        } 
        else {
            extract = "No results found";
        }
        WikiArticle article = new WikiArticle(term, extract);
        System.out.println("cpmpleted wiki req");
        System.out.println(article);
        articleRepo.save(article);
        return article;
    }
    
    @Transactional
    public SearchHistory saveHistoryWithArticles(SearchHistory history) {
        history.getArticles().forEach(article -> article.setHistory(history));
        return historyRepo.save(history);
    }

    
    public List<SearchHistory> getAllHistories() {
        return historyRepo.findAll();
    }
}