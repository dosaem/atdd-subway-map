package subway.api.domain.service;

import subway.api.domain.dto.inport.SectionCreateCommand;
import subway.api.domain.dto.outport.SectionInfo;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
public interface SectionService {
	SectionInfo addSection(Long lineId, SectionCreateCommand createRequest);

	void deleteSection(Long lineId, Long stationId);
}
