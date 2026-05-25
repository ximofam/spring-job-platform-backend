package db.seed.postgresql;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class V8__seed_locations extends BaseJavaMigration {

    private static final String JSON_PATH = "/db/data/locations.json";

    @Override
    public void migrate(Context context) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        List<CityJson> cities;
        try (InputStream is = getClass().getResourceAsStream(JSON_PATH)) {
            if (is == null) {
                throw new IllegalStateException("Seed file not found on classpath: " + JSON_PATH);
            }
            cities = mapper.readValue(is, new TypeReference<List<CityJson>>() {
            });
        }

        Connection conn = context.getConnection();
        Timestamp now = Timestamp.from(Instant.now());

        String insertCity =
                "INSERT INTO cities (name, code) " +
                        "VALUES (?, ?) " +
                        "ON CONFLICT (code) DO NOTHING";

        String selectCityId =
                "SELECT id FROM cities WHERE code = ?";

        String insertDistrict =
                "INSERT INTO districts (name, code, city_id) " +
                        "VALUES (?, ?, ?) " +
                        "ON CONFLICT DO NOTHING";

        for (CityJson city : cities) {
            try (PreparedStatement ps = conn.prepareStatement(insertCity)) {
                ps.setString(1, city.name);
                ps.setString(2, String.valueOf(city.code));
                ps.executeUpdate();
            }

            Integer cityId;
            try (PreparedStatement ps = conn.prepareStatement(selectCityId)) {
                ps.setString(1, String.valueOf(city.code));
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalStateException(
                                "City not found after insert, code=" + city.code);
                    }
                    cityId = rs.getInt("id");
                }
            }

            if (city.districts == null || city.districts.isEmpty()) continue;

            try (PreparedStatement ps = conn.prepareStatement(insertDistrict)) {
                for (DistrictJson district : city.districts) {
                    ps.setString(1, district.name);
                    ps.setString(2, String.valueOf(district.code));
                    ps.setObject(3, cityId);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class CityJson {
        @JsonProperty("code")
        public int code;

        @JsonProperty("name")
        public String name;

        @JsonProperty("districts")
        public List<DistrictJson> districts;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class DistrictJson {
        @JsonProperty("code")
        public int code;

        @JsonProperty("name")
        public String name;
    }
}