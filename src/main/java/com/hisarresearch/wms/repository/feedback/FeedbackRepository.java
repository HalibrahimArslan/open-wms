package com.hisarresearch.wms.repository.feedback;

import com.hisarresearch.wms.domain.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> , JpaSpecificationExecutor<Feedback> {
}
