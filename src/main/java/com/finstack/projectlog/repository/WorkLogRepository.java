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
    
//    @Query("SELECT w.resourceId, SUM(w.totalTime) FROM WorkLog w WHERE w.workDate = CURRENT_DATE GROUP BY w.resourceId")
//    List<Object[]> findTotalTimeGroupedByResourceIdForToday();
    
    @Query("SELECT w.resourceId, w.totalTime FROM WorkLog w WHERE w.workDate = CURRENT_DATE")
    List<Object[]> findLogsForDaily();
    
    @Query("SELECT w.resourceId, w.totalTime FROM WorkLog w WHERE EXTRACT(YEAR FROM w.workDate) = EXTRACT(YEAR FROM CURRENT_DATE)")
    List<Object[]> findLogsForYear();
   
    @Query("SELECT w.resourceId, w.totalTime FROM WorkLog w " +
            "WHERE EXTRACT(YEAR FROM w.workDate) = EXTRACT(YEAR FROM CURRENT_DATE) " +
            "AND EXTRACT(MONTH FROM w.workDate) = EXTRACT(MONTH FROM CURRENT_DATE)")
     List<Object[]> findLogsForMonth();  
    
//     @Query("SELECT w.resourceId, w.totalTime FROM WorkLog w " +
//             "WHERE w.workDate >= DATE_TRUNC('week', CURRENT_DATE) " +
//             "AND w.workDate < DATE_TRUNC('week', CURRENT_DATE) + INTERVAL '1 week'")
//      List<Object[]> findLogsForWeek();
      
      @Query(value = "SELECT w.resource_id, w.total_time FROM WORKDETAILS.WORKLOG_TABLE w " +
              "WHERE w.work_date >= DATE_TRUNC('week', CURRENT_DATE) " +
              "AND w.work_date < DATE_TRUNC('week', CURRENT_DATE) + INTERVAL '1 week'", 
      nativeQuery = true)
      List<Object[]> findLogsForWeek();

      @Query(value = "SELECT w.resource_id, w.total_time FROM WORKDETAILS.WORKLOG_TABLE w " +
              "WHERE w.work_date >= DATE_TRUNC('week', CURRENT_DATE) " +
              "AND w.work_date < DATE_TRUNC('week', CURRENT_DATE) + INTERVAL '5 days'", 
      nativeQuery = true)
      List<Object[]> findLogsForWeekdays();
  
      //  worklog queries for daily,weekly,monthly and yearly without consolidation  
      @Query("SELECT w FROM WorkLog w WHERE w.workDate = CURRENT_DATE")
      List<WorkLog> findDailyWorkLogs();
      
      @Query(value = "SELECT w FROM WORKDETAILS.WORKLOG_TABLE w " +
              "WHERE w.work_date >= DATE_TRUNC('week', CURRENT_DATE) " +
              "AND w.work_date < DATE_TRUNC('week', CURRENT_DATE) + INTERVAL '5 days'", 
      nativeQuery = true)
      List<WorkLog> findWeeklyWorkLogs();
      
      @Query("SELECT w FROM WorkLog w " +
              "WHERE EXTRACT(YEAR FROM w.workDate) = EXTRACT(YEAR FROM CURRENT_DATE) " +
              "AND EXTRACT(MONTH FROM w.workDate) = EXTRACT(MONTH FROM CURRENT_DATE)")
       List<WorkLog> findMonthlyWorkLogs();

      
      @Query("SELECT w FROM WorkLog w WHERE EXTRACT(YEAR FROM w.workDate) = EXTRACT(YEAR FROM CURRENT_DATE)")
      List<WorkLog> findYearlyWorkLogs();
}
