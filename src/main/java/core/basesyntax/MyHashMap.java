package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int INITIAL_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_MULTIPLIER = 2;
    private Node<K, V>[] table;
    private int threshold;
    private int size;

    public MyHashMap() {
        table = new Node[INITIAL_CAPACITY];
        threshold = calculateThreshold(INITIAL_CAPACITY);
    }

    @Override
    public void put(K key, V value) {
        if (size >= threshold) {
            resize();
        }
        Node currentNode = table[getBucketIndex(hash(key))];
        while (currentNode != null) {
            if (hash((K) currentNode.key) == (hash(key))
                    && (currentNode.key == key
                    || (currentNode.key != null && currentNode.key.equals(key)))) {
                currentNode.value = value;
                return;
            }
            if (currentNode.next == null) {
                currentNode.next = new Node<>(hash(key), key, value, null);
                size++;
                return;
            }
            currentNode = currentNode.next;
        }
        table[getBucketIndex(hash(key))] = new Node<>(hash(key), key, value, null);
        size++;
    }

    @Override
    public V getValue(K key) {
        int hash = hash(key);
        int index = getBucketIndex(hash);
        Node currentNode = table[index];
        while (currentNode != null) {
            if (hash((K) currentNode.key) == (hash(key))
                    && (currentNode.key == key
                    || (currentNode.key != null && currentNode.key.equals(key)))) {
                return (V) currentNode.value;
            }
            currentNode = currentNode.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int capacity = table.length * DEFAULT_MULTIPLIER;
        size = 0;
        Node<K, V>[] newTable = table;
        table = new Node[capacity];
        threshold = calculateThreshold(capacity);
        transfer(newTable);
    }

    private Node<K, V>[] transfer(Node<K, V>[] newTable) {
        for (Node<K, V> node : newTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
        return newTable;
    }

    private int hash(K key) {
        return Math.abs((key == null) ? 0 : key.hashCode());
    }

    private int getBucketIndex(int hash) {
        return hash % table.length;
    }

    private int calculateThreshold(int capacity) {
        return (int) (capacity * LOAD_FACTOR);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        private Node(int hash, K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
