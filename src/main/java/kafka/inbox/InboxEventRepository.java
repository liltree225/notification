package kafka.inbox;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InboxEventRepository extends JpaRepository<InboxEvent, UUID> {
    @Query("SELECT i FROM InboxEvent i WHERE i.isProcessed = false AND i.status = 'RECEIVED' ORDER BY i.createdAt ASC")
    List<InboxEvent> findUnprocessedEvents(Pageable pageable);

    @Query("SELECT COUNT(i) FROM InboxEvent i WHERE i.isProcessed = false")
    long countUnprocessed();

    @Query("SELECT i FROM InboxEvent i WHERE i.aggregateId = ?1 AND i.aggregateType = ?2")
    Optional<InboxEvent> findByAggregateIdAndType(UUID aggregateId, String aggregateType);
}

