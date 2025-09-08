package com.example.qe.queryengine.query;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryContextDto {
    String tableName;
    JsonNode json;
    String createdBy;
    String spoofDate;
}
