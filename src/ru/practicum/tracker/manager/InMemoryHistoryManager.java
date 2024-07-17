package ru.practicum.tracker.manager;

import ru.practicum.tracker.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private Node first;
    private Node last;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (nodeMap.containsKey(task.getUniqueID())) {
            removeNode(task.getUniqueID());
        }
        linkLast(task);
    }

    @Override
    public void remove (int id) {
        removeNode(id);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        Node node;
        if (last != null && first != null) {
            node = new Node(null, last, task);
            last.next = node;
        } else {
            node = new Node(null, null, task);
            first = node;
        }
        last = node;
        nodeMap.put(node.value.getUniqueID(), node);
    }

    private void removeNode(int id) {
        Node node = nodeMap.remove(id);

        if (node == first) {
            first = first.next;
        } else if (node == last) {
            last = last.prev;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;;
        }
        node.value = null;

    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Node current = first; current != null; current = current.next) {
            if (current.value != null)
                allTasks.add(current.value);
        }
        return allTasks;
    }

    private static class Node {
        Node prev;
        Node next;
        Task value;

        public Node (Node next, Node prev, Task value) {
            this.next = next;
            this.prev = prev;
            this.value = value;
        }
    }
}
