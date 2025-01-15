package com.employee.recordsystem.service.impl;

import com.employee.recordsystem.model.AuditLog;
import com.employee.recordsystem.model.User;
import com.employee.recordsystem.repository.AuditLogRepository;
import com.employee.recordsystem.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional
    public void logAction(String entityType, Long entityId, String action, String changes) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setAction(action);
        auditLog.setChanges(changes);
        auditLog.setUser(currentUser);
        auditLog.setTimestamp(LocalDateTime.now());
        
        auditLogRepository.save(auditLog);
    }

    @Override
    public List<String> getAuditTrail(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityIdOrderByTimestampDesc(entityType, entityId)
            .stream()
            .map(this::formatAuditLog)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> getAuditTrailByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByTimestampBetween(startDate, endDate)
            .stream()
            .map(this::formatAuditLog)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> getUserActions(Long userId) {
        return auditLogRepository.findByUserId(userId)
            .stream()
            .map(this::formatAuditLog)
            .collect(Collectors.toList());
    }

    private String formatAuditLog(AuditLog auditLog) {
        return String.format("[%s] %s performed %s on %s (ID: %d): %s",
            auditLog.getTimestamp(),
            auditLog.getUser().getUsername(),
            auditLog.getAction(),
            auditLog.getEntityType(),
            auditLog.getEntityId(),
            auditLog.getChanges());
    }
}
