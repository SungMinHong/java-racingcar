package step3.application;

import common.util.Message;
import step3.domain.*;
import step3.domain.strategy.RandomMovableStrategy;

import java.util.List;
import java.util.Objects;

import static common.util.Preconditions.checkArgument;
import static step3.application.SimulationResponseInterator.ErrorMessage.NOT_BE_NULL;

public class SimulationResponseInterator {

    public enum ErrorMessage implements Message {
        NOT_BE_NULL(SimulationResponseInterator.class.getName() + "'s value should not be null"),
        ;

        private final String message;

        public String getMessage() {
            return message;
        }

        ErrorMessage(String message) {
            this.message = message;
        }
    }

    public SimulationResponse interact(final SimulationRequest request) {
        checkArgument(Objects.nonNull(request), NOT_BE_NULL);

        final List<String> carNames = request.getCarNames();
        final int times = request.getNumberAttempts();
        final RacingGame racingGame = RacingGame.of(carNames, new RandomMovableStrategy());

        return getResponseAfterGameFinished(racingGame, times);
    }

    private SimulationResponse getResponseAfterGameFinished(final RacingGame racingGame, final int times) {
        racingGame.run(times);
        final List<Snapshot> snapshots = racingGame.extractSnapshots();
        final List<String> winners = racingGame.selectWinnerNames();
        return new SimulationResponse(snapshots, winners);
    }
}