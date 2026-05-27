package db.seed.postgresql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class V12__seed_categories extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<String>> categories;

        try (InputStream is = getClass().getResourceAsStream("/db/data/categories.json")) {
            if (is == null) {
                throw new IllegalStateException("Không tìm thấy file categories.json trong resources");
            }
            categories = mapper.readValue(is, new TypeReference<LinkedHashMap<String, List<String>>>() {
            });
        }

        String insertParent = """
                INSERT INTO categories (name, parent_id, created_at, updated_at)
                VALUES (?, NULL, NOW(), NOW())
                """;

        String insertChild = """
                INSERT INTO categories (name, parent_id, created_at, updated_at)
                VALUES (?, ?, NOW(), NOW())
                """;

        String getLastId = "SELECT lastval()";

        try (PreparedStatement parentStmt = context.getConnection().prepareStatement(insertParent);
             PreparedStatement childStmt = context.getConnection().prepareStatement(insertChild);
             PreparedStatement lastIdStmt = context.getConnection().prepareStatement(getLastId)) {

            for (Map.Entry<String, List<String>> entry : categories.entrySet()) {
                parentStmt.setString(1, entry.getKey());
                parentStmt.executeUpdate();

                int parentId;
                try (ResultSet rs = lastIdStmt.executeQuery()) {
                    rs.next();
                    parentId = rs.getInt(1);
                }

                for (String childName : entry.getValue()) {
                    childStmt.setString(1, childName);
                    childStmt.setInt(2, parentId);
                    childStmt.executeUpdate();
                }
            }
        }
    }
}