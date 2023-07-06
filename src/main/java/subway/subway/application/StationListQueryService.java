package subway.subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.subway.application.in.StationListQuery;
import subway.subway.application.out.StationListQueryPort;
import subway.subway.application.query.StationResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
class StationListQueryService implements StationListQuery {

    private final StationListQueryPort stationListQueryPort;

    StationListQueryService(StationListQueryPort stationListQueryPort) {
        this.stationListQueryPort = stationListQueryPort;
    }

    @Override
    public List<StationResponse> findAll() {
        return stationListQueryPort.findAll();
    }
}
