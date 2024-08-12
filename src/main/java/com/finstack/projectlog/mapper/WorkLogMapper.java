package com.finstack.projectlog.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.finstack.projectlog.entity.WorkLog;
import com.finstack.projectlog.model.WorkLogDTO;

//@Mapper(componentModel = "spring")
@Mapper
public interface WorkLogMapper {

	WorkLogDTO workLogToWorkLogDTO(WorkLog workLog);
	WorkLog workLogDTOToWorkLog(WorkLogDTO workLogDTO);
	List<WorkLogDTO> workLogsToWorkLogDTOs(List<WorkLog> workLogs);
	List<WorkLog> workLogDTOsToWorkLogs(List<WorkLogDTO> workLogDTOs);
	
	//Page<WorkLogDTO> workLogsToWorkLogDTOsPaging(Page<WorkLog> worklogs);
	//Page<WorkLog> workLogDTOsToWorkLogs(Page<WorkLogDTO> workLogDTOs);
	
    default Page<WorkLogDTO> workLogsToWorkLogDTOsPaging(Page<WorkLog> workLogs) {
        List<WorkLogDTO> dtos = workLogs.getContent().stream()
                .map(this::workLogToWorkLogDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, workLogs.getPageable(), workLogs.getTotalElements());
    }

    default Page<WorkLog> workLogDTOsToWorkLogs(Page<WorkLogDTO> workLogDTOs) {
        List<WorkLog> entities = workLogDTOs.getContent().stream()
                .map(this::workLogDTOToWorkLog)
                .collect(Collectors.toList());
        return new PageImpl<>(entities, workLogDTOs.getPageable(), workLogDTOs.getTotalElements());
    }
	
}
