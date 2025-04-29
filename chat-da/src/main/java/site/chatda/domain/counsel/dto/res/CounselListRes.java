package site.chatda.domain.counsel.dto.res;

import lombok.Data;
import site.chatda.domain.counsel.dto.CounselDto;
import site.chatda.domain.counsel.entity.Counsel;

import java.util.List;

@Data
public class CounselListRes {

    private List<CounselDto> counsels;

    public CounselListRes(List<Counsel> counsels) {
        this.counsels = counsels.stream()
                .map(CounselDto::new)
                .toList();
    }
}
