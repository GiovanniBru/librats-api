package org.librats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankingDTO {
    private String displayName;
    private Integer totalPoints;
    private Integer totalPages;
    private Integer progressPercentage;
}