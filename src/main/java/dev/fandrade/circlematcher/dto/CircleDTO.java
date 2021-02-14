package dev.fandrade.circlematcher.dto;

import dev.fandrade.circlematcher.infra.DTO;
import dev.fandrade.circlematcher.infra.ValidDTO;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@ValidDTO
public class CircleDTO implements DTO {

    @NotNull
    public String name;
    public String circleId;
    public String workspaceId;
    @NotNull
    public String match;
    public Boolean enabled;
    public Boolean isDefault;
}
