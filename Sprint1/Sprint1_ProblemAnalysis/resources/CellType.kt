import java.awt.Color

enum class CellType(val type: String, val color: Color = Color.WHITE) {
    EMPTY("0"),
    BORDER("1"),
    ROBOT("r"),
    HOME("H", Color.RED),
    INDOOR("I", Color.CYAN),
    GLASS("G", Color.GREEN),
    PLASTIC("P", Color.YELLOW),
    PAPER("p", Color.BLUE),
    METAL("M", Color.GRAY),
    ORGANIC("O", Color(102,51,0)),
}