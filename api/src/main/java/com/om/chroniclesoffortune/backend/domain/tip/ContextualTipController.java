package com.om.chroniclesoffortune.backend.domain.tip;

import com.om.chroniclesoffortune.backend.domain.tip.dto.ContextualTipResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@NullMarked
@RestController
@RequestMapping("/contextual-tips")
@RequiredArgsConstructor
public class ContextualTipController {

    private final ContextualTipService contextualTipService;

    @GetMapping
    public ResponseEntity<List<ContextualTipResponse>> findByContext(
            @RequestParam String context) {
        return ResponseEntity.ok(contextualTipService.findByContext(context));
    }
}
