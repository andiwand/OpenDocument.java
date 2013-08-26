package at.stefl.opendocument.java.test;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import at.stefl.commons.util.array.ArrayUtil;
import at.stefl.opendocument.java.odf.LocatedOpenDocumentFile;
import at.stefl.opendocument.java.odf.OpenDocumentType;

public class TestFile {
    
    private static final Pattern FILE_PATTERN = Pattern
            .compile("^(.*?)-(.*?)-([1-9][0-9]*)(?:\\$(.*)\\$)?\\.(f?od[tspbgf])$");
    private static final int GROUP_GROUP = 1;
    private static final int DESCRIPTION_GROUP = 2;
    private static final int ID_GROUP = 3;
    private static final int PASSWORD_GROUP = 4;
    private static final int TYPE_GROUP = 5;
    
    private static final Pattern SIMPLE_FILE_PATTERN = Pattern
            .compile("^(.*)\\.(f?od[tspbgf])$");
    private static final int SIMPLE_DESCRIPTION_GROUP = 1;
    private static final int SIMPLE_TYPE_GROUP = 2;
    
    private static final String GROUP_SEPARATOR = "+";
    
    public static TestFile fromPattern(File file) {
        Matcher matcher = FILE_PATTERN.matcher(file.getName());
        
        Set<String> groups = null;
        String description = null;
        int id = 0;
        String password = null;
        OpenDocumentType type = null;
        
        if (!matcher.matches()) {
            matcher = SIMPLE_FILE_PATTERN.matcher(file.getName());
            if (!matcher.matches()) return null;
            
            description = matcher.group(SIMPLE_DESCRIPTION_GROUP);
            type = OpenDocumentType.getByExtension(matcher
                    .group(SIMPLE_TYPE_GROUP));
        } else {
            groups = ArrayUtil.toHashSet(matcher.group(GROUP_GROUP).split(
                    Pattern.quote(GROUP_SEPARATOR)));
            description = matcher.group(DESCRIPTION_GROUP);
            id = Integer.parseInt(matcher.group(ID_GROUP));
            password = (matcher.groupCount() >= TYPE_GROUP) ? matcher
                    .group(PASSWORD_GROUP) : null;
            type = OpenDocumentType.getByExtension(matcher.group(TYPE_GROUP));
        }
        
        return new TestFile(file, groups, description, id, password, type);
    }
    
    private final File file;
    private final Set<String> groups;
    private final String description;
    private final int id;
    private final String password;
    private final OpenDocumentType type;
    
    private TestFile(File file, Set<String> groups, String description, int id,
            String password, OpenDocumentType type) {
        this.file = file;
        this.groups = Collections.unmodifiableSet(groups);
        this.description = description;
        this.id = id;
        this.password = password;
        this.type = type;
    }
    
    @Override
    public String toString() {
        return file.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof TestFile)) return false;
        return file.equals(((TestFile) obj).file);
    }
    
    @Override
    public int hashCode() {
        return file.hashCode();
    }
    
    public File getFile() {
        return file;
    }
    
    public Set<String> getGroups() {
        return groups;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getId() {
        return id;
    }
    
    public String getPassword() {
        return password;
    }
    
    public OpenDocumentType getType() {
        return type;
    }
    
    public LocatedOpenDocumentFile getDocumentFile() throws IOException {
        LocatedOpenDocumentFile result = new LocatedOpenDocumentFile(file);
        result.setPassword(password);
        return result;
    }
    
}