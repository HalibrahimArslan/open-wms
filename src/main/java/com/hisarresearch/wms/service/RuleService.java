package com.hisarresearch.wms.service;

import com.hisarresearch.wms.domain.MailList;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import com.hisarresearch.wms.domain.Rule;
import com.hisarresearch.wms.repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RuleService {

    private final RuleRepository ruleRepository;

    private final AurLogService aurLogService;

    @Autowired
    public RuleService(RuleRepository ruleRepository, AurLogService aurLogService) {
        this.ruleRepository = ruleRepository;
        this.aurLogService = aurLogService;
    }

    public List<Rule> getAllRules() {
        return ruleRepository.findAll();
    }

    public Rule saveRule(Rule rule) {
        return ruleRepository.save(rule);
    }

    public Optional<Rule> getRuleById(long id) {
        return ruleRepository.findById(id);
    }

    public KieSession getKieSession() {
        KieHelper kieHelper = new KieHelper();
        for (Rule rule : getAllRules()) {
            kieHelper.addContent(rule.getRuleContent(), ResourceType.DRL);
        }
        return kieHelper.build().newKieSession();
    }

    public void executeRules(Object fact,String ruleGroupName) {
        long logId = aurLogService.logRequest("executeRule", "executeRule", "Rules is started");

        try {
            KieSession kieSession = getKieSession();
            kieSession.insert(fact);
            kieSession.getAgenda().getAgendaGroup(ruleGroupName).setFocus();
            kieSession.fireAllRules();
            kieSession.dispose();
            aurLogService.logResponse(logId, "Rules executed successfully");
        } catch (Exception e) {
            aurLogService.logResponse(logId, "Rules execution failed" + e.getMessage());
        }
    }

    public MailList executeRulesForMail(Object fact, String ruleGroupName) {
        MailList mailList = new MailList();
        long logId = aurLogService.logRequest("executeRulesForMail", "executeRulesForMail", "Mail Rules is started");

        try {
            KieSession kieSession = getKieSession();
            kieSession.setGlobal("mailList", mailList);
            kieSession.insert(fact);

            // Belirli bir kural grubunu çalıştırma
            kieSession.getAgenda().getAgendaGroup(ruleGroupName).setFocus();

            kieSession.fireAllRules();
            kieSession.dispose();
            aurLogService.logResponse(logId, "Mail rules executed successfully");
            return mailList;
        } catch (Exception e) {
            aurLogService.logResponse(logId, "Mail rules execution failed" + e.getMessage());
            return mailList;
        }
    }


}
