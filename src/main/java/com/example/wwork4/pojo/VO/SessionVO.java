package com.example.wwork4.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionVO {
    private String contactName;
    private Integer contactId;
    private String sessionId;
    private String lastSenderName;
    private Integer unReadCount;
}
