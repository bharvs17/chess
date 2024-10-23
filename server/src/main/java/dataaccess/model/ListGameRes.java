package dataaccess.model;

import model.GameData;
import java.util.Collection;

public record ListGameRes(Collection<GameData> games) {
}
