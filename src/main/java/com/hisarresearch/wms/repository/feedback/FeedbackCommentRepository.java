package com.hisarresearch.wms.repository.feedback;

import com.hisarresearch.wms.domain.FeedbackComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackCommentRepository extends JpaRepository<FeedbackComment, Long> {
}
