package wikisearch.wiki_search.controller;

import wikisearch.wiki_search.entity.WikiArticle;
import wikisearch.wiki_search.service.WikiArticleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class WikiController {
    private final WikiArticleService wikiArticleService;

    public WikiController(WikiArticleService wikiArticleService) {
        this.wikiArticleService = wikiArticleService;
    }

    @GetMapping("/search")
    public WikiArticle search(@RequestParam String term) {
        return wikiArticleService.search(term);
    }
}