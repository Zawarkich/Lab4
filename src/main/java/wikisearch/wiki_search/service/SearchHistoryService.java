package wikisearch.wiki_search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wikisearch.wiki_search.cache.SimpleCache;
import wikisearch.wiki_search.entity.SearchHistory;
import wikisearch.wiki_search.repository.SearchHistoryRepository;
import java.util.List;

@Service
public class SearchHistoryService {
    private final SearchHistoryRepository historyRepo;
    private final SimpleCache cache;

    @Autowired
    public SearchHistoryService(SearchHistoryRepository historyRepo, SimpleCache cache) {
        this.historyRepo = historyRepo;
        this.cache = cache;
    }

    @Transactional
    public SearchHistory saveHistoryWithArticles(SearchHistory history) {
        history.getArticles().forEach(article -> article.setHistory(history));
        SearchHistory saved = historyRepo.save(history);
        cache.put("history_" + saved.getId(), saved);
        return saved;
    }

    @SuppressWarnings("unchecked")
    public List<SearchHistory> getAllHistories() {
        String key = "all_histories";
        List<SearchHistory> cached = (List<SearchHistory>) cache.get(key);
        if (cached != null) {
            return cached;
        }
        List<SearchHistory> histories = historyRepo.findAll();
        cache.put(key, histories);
        return histories;
    }
}
