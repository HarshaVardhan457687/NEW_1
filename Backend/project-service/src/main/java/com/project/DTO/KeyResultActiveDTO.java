package com.project.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KeyResultActiveDTO {
    private Long keyResultId;
    private String keyResultName;
    private int keyResultcurrentVal;
    private int keyResultTargetVal;
}
