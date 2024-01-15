package org.example.application.example.dto.command;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ExampleModifyAddressCommand {
    @NotNull(message = "请在地图上选择位置")
    private Double longitude;

    @NotNull(message = "请在地图上选择位置")
    private Double latitude;
}
