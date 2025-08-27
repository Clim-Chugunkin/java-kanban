package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_DEPTH = 10;
    private final Map<Integer, Node<Task>> history = new HashMap<>();
    private Node<Task> tail;
    private Node<Task> head;
    private int size = 0;

    @Override
    public Task addTask(Task task) {

        if (task == null) return null;

        //если в истории уже есть  задача task
        if (history.containsKey(task.getTaskID())) {
            //удаляем задачу из св_списка
            removeNode(history.get(task.getTaskID()));
        }

        // добавляем задачу в св_список
        // и обновляем _history
        history.put(task.getTaskID(), linkLast(task));
        return task;
    }

    @Override
    public void remove(int id) {
        //если история содержит удаляемую задачу
        if (history.containsKey(id)) {
            //удаляем из списка
            removeNode(history.get(id));
            //удаляем из таблицы
            history.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }


    private Node<Task> linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(tail, task, null);
        tail = newNode;

        if (oldTail == null)
            head = newNode;
        else
            oldTail.setNext(newNode);
        size++;
        return newNode;
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node<Task> next = head;
        while (next != null) {
            tasks.add(next.getData());
            next = next.getNext();
        }
        return tasks;
    }

    private void removeNode(Node<Task> delNode) {
        if (delNode != head) {
            delNode.getPrev().setNext(delNode.getNext());
        } else {
            head = head.getNext();
        }

        if (delNode != tail) {
            delNode.getNext().setPrev(delNode.getPrev());
        } else {
            tail = tail.getPrev();
        }
    }
}
