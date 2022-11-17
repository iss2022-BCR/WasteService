package it.unibo.map_editor_bcr.model.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that contains a buffer for map operations (MapOperation objects). It allows the user to:
 * - execute new operations;
 * - undo operations;
 * - redo operations.
 * This is achieved through the use of an index that keeps track of the current position inside the operations buffer.
 */
public class MapConfigOperationExecutor {
    private final List<MapConfigOperation> mapOperations = new ArrayList<MapConfigOperation>();
    private int currentIndex = -1;

    /**
     * Executes a MapOperation and add it to the buffer. If the operations overwrites existing operations (e.g. when the
     * buffer counter <i>currentIndex</i> is less than the total size of the buffer, which happens when the user has
     * undone one or more operations, without redoing them), all the following operations are removed from the buffer,
     * and the new one is added at the end of it.
     */
    public String executeOperation(MapConfigOperation operation) {
        if (this.currentIndex < this.mapOperations.size()-1) {
            for(int i = this.currentIndex+1; i < this.mapOperations.size();) {
                this.mapOperations.remove(i);
            }
            this.currentIndex = this.mapOperations.size()-1;
        }
        this.mapOperations.add(operation);
        operation.execute();
        this.currentIndex++;

        return "Exec: " + operation.getDescription();
    }
    /**
     * Undoes a MapOperation and decrease the buffer counter <i>currentIndex</i> by 1.
     */
    public String undoOperation() {
        String res = "Undo: " + this.mapOperations.get(this.currentIndex).getDescription();
        this.mapOperations.get(this.currentIndex).undo();
        this.currentIndex--;

        return res;
    }
    /**
     * Redoes a MapOperation and increase the buffer counter <i>currentIndex</i> by 1.
     */
    public String redoOperation() {
        if(this.currentIndex == this.mapOperations.size()-1)
            return "";
        this.currentIndex++;
        this.mapOperations.get(this.currentIndex).execute();

        return "Redo: " + this.mapOperations.get(this.currentIndex).getDescription();
    }
    public boolean canUndo() {
        return this.currentIndex >= 0;
    }
    public boolean canRedo() {
        return this.currentIndex < this.mapOperations.size()-1;
    }
    public List<String> getOperationsList() {
        List<String> result = new ArrayList<String>();
        for(int i = 0; i < this.mapOperations.size(); i++) {
            result.add((this.currentIndex == i ? "> " : "  ") +
                    i + ") " + this.mapOperations.get(i).getDescription());
        }
        return result;
    }
}
