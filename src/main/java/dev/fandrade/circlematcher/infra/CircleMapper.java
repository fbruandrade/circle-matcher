package dev.fandrade.circlematcher.infra;

import dev.fandrade.circlematcher.dto.CircleDTO;
import dev.fandrade.circlematcher.model.Circle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface CircleMapper {

    @Mapping(target = "name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public Circle toCircle(CircleDTO dto);

}