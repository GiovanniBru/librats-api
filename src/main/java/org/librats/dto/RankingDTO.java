package org.librats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankingDTO {
    private String displayName;
    private Integer totalPoints;
    private Long booksFinished; // Um extra para o desempate!
}