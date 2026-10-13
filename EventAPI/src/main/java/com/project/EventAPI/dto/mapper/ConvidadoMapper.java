package com.project.EventAPI.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import com.project.EventAPI.dto.request.ConvidadoRequestDTO;
import com.project.EventAPI.dto.response.ConvidadoResponseDTO;
import com.project.EventAPI.dto.update.ConvidadoUpdateDTO;
import com.project.EventAPI.entity.Convidado;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConvidadoMapper {

  @Mapping(target = "eventoId", source = "evento.id")
  ConvidadoResponseDTO toResponse(Convidado convidado);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evento", ignore = true)
  @Mapping(target = "confirmado", defaultValue = "false")
  Convidado toEntity(ConvidadoRequestDTO dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evento", ignore = true)
  void updateEntity(ConvidadoUpdateDTO dto, @MappingTarget Convidado convidado);
}
