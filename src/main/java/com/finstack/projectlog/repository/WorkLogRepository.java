package com.finstack.projectlog.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finstack.projectlog.entity.Resource;
import com.finstack.projectlog.entity.WorkLog;

public interface WorkLogRepository extends JpaRepository<WorkLog, Integer> {

   // @Query("SELECT wl FROM WorkLog wl WHERE wl.resourceId = :resourceId AND wl.workLogDate = :workLogDate")
    
    @Query("SELECT wl FROM WorkLog wl " +
            "WHERE (:resourceId IS NULL OR wl.resourceId = :resourceId) " +
            "AND (cast(:workDate as date) IS NULL OR wl.workDate = :workDate)"
            )
	Page<WorkLog> findAllWorkLogs(@Param("resourceId") String resourceId,
									@Param("workDate") Date workDate,
									Pageable pageable);
	

	/*
	 * @Query("SELECT wl FROM WorkLog wl " +
	 * "WHERE (:resourceId IS NULL OR wl.resourceId = :resourceId) " +
	 * "AND (:workDate IS NULL OR CAST(wl.workDate AS date) = CAST(:workDate AS date))"
	 * ) Page<WorkLog> findAllWorkLogs(@Param("resourceId") String resourceId,
	 * 
	 * @Param("workDate") Date workDate, Pageable pageable);
	 */
    
    
    
    @Query("SELECT r FROM Resource r WHERE r.emailAddress NOT IN (SELECT w.resourceId FROM WorkLog w WHERE w.workDate = CURRENT_DATE)")
    List<Resource> findResourcesWithoutWorkLogToday();
}
