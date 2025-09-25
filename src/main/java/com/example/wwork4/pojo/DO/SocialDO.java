package com.example.wwork4.pojo.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialDO {
    private Integer user_id;
    private Integer to_user_id;
    private Integer action_type;
}
