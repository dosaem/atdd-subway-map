package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.subwayline.SubwayLineModifyRequest;
import nextstep.subway.applicaion.dto.subwayline.SubwayLineRequest;
import nextstep.subway.applicaion.dto.subwayline.SubwayLineResponse;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.SubwayLine;
import nextstep.subway.repository.StationRepository;
import nextstep.subway.repository.SubwayLineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubwayLineService {

	private final SubwayLineRepository lineRepository;
	private final StationRepository stationRepository;

	@Transactional
	public SubwayLineResponse createSubwayLine(SubwayLineRequest request) {
		Section section = new Section(request.getUpStationId(), request.getDownStationId(), request.getDistance());
		SubwayLine savedLine = lineRepository.save(request.toEntity(section));
		return new SubwayLineResponse(savedLine, findAllStations(savedLine));
	}

	@Transactional
	public void modifySubwayLine(Long id, SubwayLineModifyRequest request) {
		SubwayLine subwayLine = findSubwayLineById(id);
		subwayLine.modify(request);
	}

	@Transactional
	public void deleteSubwayLine(Long id) {
		SubwayLine subwayLine = findSubwayLineById(id);
		lineRepository.delete(subwayLine);
	}

	public List<SubwayLineResponse> findAll() {
		return lineRepository.findAll().stream()
				.map(line -> new SubwayLineResponse(
						line, findAllStations(line)))
				.collect(Collectors.toList());
	}

	public SubwayLineResponse findById(Long id) {
		SubwayLine subwayLine = findSubwayLineById(id);
		return new SubwayLineResponse(subwayLine, findAllStations(subwayLine));
	}

	private List<Station> findAllStations(SubwayLine subwayLine) {
		List<Section> sectionList = subwayLine.getSectionList();
		List<Long> stationIdList = sectionList.stream()
				.map(Section::getUpStationId)
				.collect(Collectors.toList());

		stationIdList.add(lastSection(sectionList).getDownStationId());

		return stationIdList.stream()
				.map(
						id -> stationRepository.findById(id).orElseThrow(NoSuchElementException::new)
				)
				.collect(Collectors.toList());
	}

	private Section lastSection(List<Section> sectionList) {
		return sectionList.get(sectionList.size() - 1);
	}

	private SubwayLine findSubwayLineById(Long id) {
		return lineRepository.findById(id).orElseThrow(NoSuchElementException::new);
	}
}