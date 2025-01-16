package com.employee.recordsystem.controller;

import com.employee.recordsystem.service.AuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
@Api(tags = "Audit Trail Management")
@PreAuthorize("hasRole('ADMIN')")
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/{entityType}/{entityId}")
    @ApiOperation("Get audit trail for specific entity")
    public ResponseEntity<List<String>> getAuditTrail(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        return ResponseEntity.ok(auditService.getAuditTrail(entityType, entityId));
    }

    @GetMapping("/date-range")
    @ApiOperation("Get audit trail for date range")
    public ResponseEntity<List<String>> getAuditTrailByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(auditService.getAuditTrailByDateRange(startDate, endDate));
    }

    @GetMapping("/user/{userId}")
    @ApiOperation("Get audit trail for specific user")
    public ResponseEntity<List<String>> getUserActions(@PathVariable Long userId) {
        return ResponseEntity.ok(auditService.getUserActions(userId));
    }
}
