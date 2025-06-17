package aiss.githubminer.utils;

public final class ToStringBuilder {

    private final StringBuilder sb = new StringBuilder();

    public ToStringBuilder(Object obj) {
        sb  .append(obj.getClass().getName())
            .append('@')
            .append(Integer.toHexString(System.identityHashCode(obj)))
            .append('[');
    }

    public ToStringBuilder append(String fieldName, Object value) {
        sb  .append(fieldName)
            .append('=')
            .append(value == null ? "<null>" : value)
            .append(',');
        return this;
    }

    @Override
    public String toString() {
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.setCharAt(sb.length() - 1, ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }
}
