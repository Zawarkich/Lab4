package wikisearch.wiki_search.controller;

import wikisearch.wiki_search.entity.WikiArticle;
import wikisearch.wiki_search.service.WikiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class WikiController {
    private final WikiService wikiService;

    public WikiController(WikiService wikiService) {
        this.wikiService = wikiService;
    }

    @GetMapping("/search")
    public WikiArticle search(@RequestParam String term) {
        return wikiService.search(term);
    }
}