package subway.exception;

import lombok.Getter;
import subway.exception.message.SubwayErrorCode;

@Getter
public class SubwayRuntimeException extends RuntimeException {

    private final SubwayErrorCode errorResponse;

    public SubwayRuntimeException(SubwayErrorCode errorResponse) {
        this.errorResponse = errorResponse;
    }
}
