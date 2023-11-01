package ab3;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Einfacher B-Baum Knoten
 */
public class BTreeNode {
    private List<KeyValuePair> keyValuePairs;
    private List<BTreeNode> children;

    public List<KeyValuePair> getKeyValuePairs() {
	return keyValuePairs;
    }

    public void setKeyValuePairs(List<KeyValuePair> keyValuePairs) {
	this.keyValuePairs = keyValuePairs;
    }

    public List<BTreeNode> getChildren() {
	return children;
    }

    public void setChildren(List<BTreeNode> children) {
	this.children = children;
    }

    public List<Integer> getKeys() {
	return getKeyValuePairs().stream().map(kvp -> kvp.getKey()).collect(Collectors.toList());
    }

    public List<String> getValues() {
	return getKeyValuePairs().stream().map(kvp -> kvp.getValue()).collect(Collectors.toList());
    }

    public static class KeyValuePair {
	private Integer key;
	private String value;

	public KeyValuePair(Integer key, String value) {
	    super();
	    this.key = key;
	    this.value = value;
	}

	public Integer getKey() {
	    return key;
	}

	public String getValue() {
	    return value;
	}

	public void setKey(Integer key) {
	    this.key = key;
	}

	public void setValue(String value) {
	    this.value = value;
	}

    }
}
