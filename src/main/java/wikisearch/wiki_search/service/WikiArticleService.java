package wikisearch.wiki_search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wikisearch.wiki_search.cache.SimpleCache;
import wikisearch.wiki_search.entity.WikiArticle;
import wikisearch.wiki_search.repository.WikiArticleRepository;

@Service
public class WikiArticleService {
    private final WikiArticleRepository articleRepo;
    private final RestTemplate restTemplate;
    private final SimpleCache cache;

    @Autowired
    public WikiArticleService(WikiArticleRepository articleRepo, SimpleCache cache) {
        this.articleRepo = articleRepo;
        this.cache = cache;
        this.restTemplate = new RestTemplate();
    }

    public WikiArticle search(String term) {
        WikiArticle cached = (WikiArticle) cache.get(term);
        if (cached != null) {
            return cached;
        }
        String url = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&exintro=true&explaintext=true&titles=" + term;
        String response = restTemplate.getForObject(url, String.class);
        String extract;
        if (response != null && response.contains("extract")) {
            extract = response.split("\"extract\":\"")[1].split("\"")[0];
        } else {
            extract = "No results found";
        }
        WikiArticle article = new WikiArticle(term, extract);
        articleRepo.save(article);
        cache.put(term, article);
        return article;
    }
}
