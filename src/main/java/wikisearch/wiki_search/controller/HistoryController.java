package wikisearch.wiki_search.controller;

import org.springframework.web.bind.annotation.*;
import wikisearch.wiki_search.entity.SearchHistory;
import wikisearch.wiki_search.service.WikiService;
import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {
    private final WikiService wikiService;

    public HistoryController(WikiService wikiService) {
        this.wikiService = wikiService;
    }

    @PostMapping
    public SearchHistory create(@RequestBody SearchHistory history) {
        return wikiService.saveHistoryWithArticles(history);
    }

    @GetMapping
    public List<SearchHistory> getAll() {
        return wikiService.getAllHistories();
    }
}