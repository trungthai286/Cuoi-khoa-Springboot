package org.example.story_web.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StoriesType {
    TRANSLATING("Đang dịch"),
    COMPLETED ("Hoàn thành"),
    PAUSE ("Tạm dừng");

    private String name;
}
