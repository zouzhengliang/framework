package greatbone.framework.web;

/**
 * A static resource.
 */
class WebStatic {

    final String key;

    // content type
    final String ctype;

    // direct byte buffer for  content
    final byte[] content;

    public WebStatic(String key, byte[] content) {
        this.key = key;
        final int dot = key.lastIndexOf('.');
        final String extension = key.substring(dot + 1);
        this.ctype = WebUtility.getMimeType(extension);
        this.content = content;
    }

    final String key() {
        return key;
    }

    final String ctype() {
        return ctype;
    }

    final int length() {
        return content.length;
    }

    final byte[] content() {
        return content;
    }

}
