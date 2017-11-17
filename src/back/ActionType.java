package back;

/**
 * Enum que contiene los distintos tipos de acciones que puede desencadenar el servidor y que los clientes deberán reflejar
 */
public enum ActionType {
    MOVEMENT, STRIKE, GETHIT, EVADE, CASTSPELL, RECEIVESPELL, STARTTURN, ENDTURN, WIN, LOSE;
}
