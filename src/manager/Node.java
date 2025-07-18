package manager;

public class Node<T> {
    private  Node<T> prev;
    private  Node<T> next;
    T data;

    public Node(Node<T> prev, T data, Node<T> next){
        this.prev = prev;
        this.next = next;
        this.data = data;
    }

    public Node<T> getPrev() {
        return prev;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next){
        this.next =next;
    }

    public void setPrev(Node<T> prev){
        this.prev = prev;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
