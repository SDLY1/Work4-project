package com.example.wwork4.pojo.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocialDO {
    Integer user_id;
    Integer to_user_id;
    Integer action_type;
}
