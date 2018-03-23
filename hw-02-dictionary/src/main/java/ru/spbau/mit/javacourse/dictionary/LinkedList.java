package ru.spbau.mit.javacourse.dictionary;

public class LinkedList {

    private Node head;
    private int size;

    public LinkedList() {
        size = 0;
    }

    public boolean contains(final String key) {
        Node currentNode = head;
        while (currentNode != null) {
            if (currentNode.getKey().equals(key)) {
                return true;
            }
            currentNode = currentNode.getNext();
        }
        return false;
    }

    public String put(final String key, final String value) {
        if (key == null) {
            return null;
        }
        final String oldValue = get(key);
        if (head == null) {
            head = new Node(key, value);
        } else {
            Node node = new Node(key, value);
            node.setNodeBefore(head);
            head = node;
        }
        size++;
        return oldValue;
    }

    public String put(final Node node) {
        return put(node.getKey(), node.getValue());
    }

    public String get(final String key) {
        Node currentNode = head;
        while (currentNode != null) {
            if (currentNode.getKey().equals(key)) {
                return currentNode.getValue();
            }
            currentNode = currentNode.getNext();
        }
        return null;
    }

    public String remove(final String key) {
        if (!contains(key)) {
            return null;
        }
        String returnValue = null;
        if (head.getKey().equals(key)) {
            returnValue = head.getValue();
            head = head.getNext();
        } else {
            Node currentNode = head;
            while (currentNode.getNext() != null) {
                if (currentNode.getNext().getKey().equals(key)) {
                    returnValue = currentNode.getNext().getValue();
                    currentNode.removeNext();
                    break;
                }
                currentNode = currentNode.getNext();
            }
        }
        size--;
        return returnValue;
    }

    public void clear() {
        head = null;
        size = 0;
    }

    public int size() {
        return size;
    }

    public Node getHead() {
        return head;
    }

    public class Node {
        private final String key;
        private final String value;
        private Node next;

        public Node(String key, String value) {
            this(key, value, null);
        }

        public Node(String key, String value, Node afterNode) {
            this.key = key;
            this.value = value;
            next = afterNode;
        }

        public void setNodeBefore(Node node) {
            next = node;
        }

        public void removeNext() {
            next = next.next;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public Node getNext() {
            return next;
        }
    }
}
