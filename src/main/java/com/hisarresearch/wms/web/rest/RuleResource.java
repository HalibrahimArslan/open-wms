package com.hisarresearch.wms.web.rest;

import com.hisarresearch.wms.domain.Rule;
import com.hisarresearch.wms.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RuleResource {

    private final RuleService ruleService;

    @Autowired
    public RuleResource(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @GetMapping("/rules")
    public ResponseEntity<List<Rule>> getAllRules() {
        return ResponseEntity.ok(ruleService.getAllRules());
    }

    @PostMapping("/rule")
    public ResponseEntity<Rule> createRule(@RequestBody Rule rule) {
        return ResponseEntity.ok(ruleService.saveRule(rule));
    }

    @PutMapping("rule/{id}")
    public ResponseEntity<Rule> updateRule(@PathVariable Long id, @RequestBody Rule updatedRule) {
        Rule rule = ruleService.getRuleById(id).orElseThrow(() -> new RuntimeException("Rule not found with id " + id));
        rule.setRuleName(updatedRule.getRuleName());
        rule.setRuleContent(updatedRule.getRuleContent());
        return ResponseEntity.ok(ruleService.saveRule(rule));
    }
}
