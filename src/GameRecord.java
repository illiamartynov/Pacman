import java.io.Serializable;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

class GameRecord implements Serializable {
    private String playerName;
    private int score;
    private final LocalDateTime timestamp;

    public GameRecord(String playerName, int score, LocalDateTime timestamp) {
        this.playerName = playerName;
        this.score = score;
        this.timestamp = timestamp;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GameRecord other = (GameRecord) obj;
        return score == other.score &&
                Objects.equals(playerName, other.playerName) &&
                Objects.equals(timestamp, other.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, score, timestamp);
    }

    @Override
    public String toString() {
        String formattedScore = String.format("%,d", score) + " points";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedTimestamp = timestamp.format(formatter);
        String pattern = "{0} - {1} ({2})";
        return MessageFormat.format(pattern, playerName, formattedScore, formattedTimestamp);
    }


}
