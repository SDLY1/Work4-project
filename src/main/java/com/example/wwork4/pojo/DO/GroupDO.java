package com.example.wwork4.pojo.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDO {
    private Integer id;
    private String groupName;
    private Integer leaderId;
    private Integer count;
    private String text;
    private String sessionId;
}
