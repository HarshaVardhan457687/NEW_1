package com.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyResultActiveDTO {
    private Long keyResultId;
    private String keyResultName;
    private int keyResultcurrentVal;
    private int keyResultTargetVal;

    public Long getKeyResultId() {
        return keyResultId;
    }

    public void setKeyResultId(Long keyResultId) {
        this.keyResultId = keyResultId;
    }

    public String getKeyResultName() {
        return keyResultName;
    }

    public void setKeyResultName(String keyResultName) {
        this.keyResultName = keyResultName;
    }

    public int getKeyResultcurrentVal() {
        return keyResultcurrentVal;
    }

    public void setKeyResultcurrentVal(int keyResultcurrentVal) {
        this.keyResultcurrentVal = keyResultcurrentVal;
    }

    public int getKeyResultTargetVal() {
        return keyResultTargetVal;
    }

    public void setKeyResultTargetVal(int keyResultTargetVal) {
        this.keyResultTargetVal = keyResultTargetVal;
    }
}
