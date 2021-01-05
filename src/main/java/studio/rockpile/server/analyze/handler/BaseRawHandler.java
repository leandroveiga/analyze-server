package studio.rockpile.server.analyze.handler;

import java.util.Map;

public abstract class BaseRawHandler {
    public abstract Map<String, Object> perform(Map<String, Object> data);
}
