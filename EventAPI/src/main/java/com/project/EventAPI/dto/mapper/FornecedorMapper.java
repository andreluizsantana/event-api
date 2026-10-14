package com.project.EventAPI.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import com.project.EventAPI.dto.request.FornecedorRequestDTO;
import com.project.EventAPI.dto.response.FornecedorResponseDTO;
import com.project.EventAPI.dto.update.FornecedorUpdateDTO;
import com.project.EventAPI.entity.Fornecedor;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FornecedorMapper {

  @Mapping(target = "eventoId", source = "evento.id")
  FornecedorResponseDTO toResponse(Fornecedor fornecedor);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evento", ignore = true)
  Fornecedor toEntity(FornecedorRequestDTO dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "evento", ignore = true)
  void updateEntity(FornecedorUpdateDTO dto, @MappingTarget Fornecedor fornecedor);
}
