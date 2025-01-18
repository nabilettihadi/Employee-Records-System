package com.employee.recordsystem.controller;

import com.employee.recordsystem.model.AuditLog;
import com.employee.recordsystem.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
@Tag(name = "Audit", description = "Audit log management APIs")
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/{entityType}/{entityId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get audit trail for specific entity", description = "Retrieve audit trail for a specific entity")
    public ResponseEntity<List<String>> getAuditTrail(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        return ResponseEntity.ok(auditService.getAuditTrail(entityType, entityId));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get audit trail for date range", description = "Retrieve audit trail between specified dates")
    public ResponseEntity<List<String>> getAuditTrailByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(auditService.getAuditTrailByDateRange(startDate, endDate));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get audit trail for specific user", description = "Retrieve audit trail for a specific user")
    public ResponseEntity<List<String>> getUserActions(@PathVariable Long userId) {
        return ResponseEntity.ok(auditService.getUserActions(userId));
    }
}
