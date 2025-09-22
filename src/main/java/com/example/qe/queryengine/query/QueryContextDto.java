package com.example.qe.queryengine.query;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryContextDto {
    String tableName;
    String json;
    String createdBy;
    String spoofDate;
}
