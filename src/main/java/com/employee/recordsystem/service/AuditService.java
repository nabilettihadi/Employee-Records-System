package com.employee.recordsystem.service;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditService {
    void logAction(String entityType, Long entityId, String action, String changes);
    List<String> getAuditTrail(String entityType, Long entityId);
    List<String> getAuditTrailByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<String> getUserActions(Long userId);
}
