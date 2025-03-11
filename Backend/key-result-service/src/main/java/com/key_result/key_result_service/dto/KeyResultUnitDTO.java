package com.key_result.key_result_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class KeyResultUnitDTO {
    private Long keyResultId;
    private int keyResultCurrentVal;
    private int getKeyResultTargetVal;
    private String keyResultUnit;
}
