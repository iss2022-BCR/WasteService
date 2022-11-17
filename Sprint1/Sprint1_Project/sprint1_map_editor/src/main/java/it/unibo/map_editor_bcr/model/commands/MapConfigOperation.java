package it.unibo.map_editor_bcr.model.commands;

public interface MapConfigOperation {
    public void execute();
    public void undo();
    public String getDescription();
}
