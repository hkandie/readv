package com.fhlbdm.eadvantage.web.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Forwards client-side route requests (e.g. a hard refresh on /some/deep/route) to
 * index.html so the SPA router can take over. Real static files (assets, favicon)
 * all contain a dot and never match these patterns, so Spring's default
 * classpath:/static/** resource handler keeps serving them directly.
 */
@Controller
public class SpaForwardController {

    @RequestMapping({"/{path:[^\\.]*}", "/{path:[^\\.]*}/**"})
    public String forwardToIndex() {
        return "forward:/index.html";
    }
}
