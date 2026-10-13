package com.project.EventAPI.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import com.project.EventAPI.dto.request.ItemEventoRequestDTO;
import com.project.EventAPI.dto.response.ItemEventoResponseDTO;
import com.project.EventAPI.dto.update.ItemEventoUpdateDTO;
import com.project.EventAPI.entity.ItemEvento;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemEventoMapper {

  @Mapping(target = "eventoId", source = "evento.id")
  ItemEventoResponseDTO toResponse(ItemEvento itemEvento);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evento", ignore = true)
  ItemEvento toEntity(ItemEventoRequestDTO dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evento", ignore = true)
  void updateEntity(ItemEventoUpdateDTO dto, @MappingTarget ItemEvento itemEvento);
}
