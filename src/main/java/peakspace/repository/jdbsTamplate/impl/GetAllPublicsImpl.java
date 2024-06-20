package peakspace.repository.jdbsTamplate.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import peakspace.dto.response.GetAllPublicProfileResponse;
import peakspace.enums.Tematica;
import peakspace.repository.jdbsTamplate.GetAllPublics;

import java.sql.ResultSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllPublicsImpl implements GetAllPublics {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<GetAllPublicProfileResponse> getAllPublics(Long userId) {
        String sql = "SELECT pp.id, pp.avatar, pp.pablic_name, pp.tematica, u.user_name AS owner " +
                "FROM pablic_profiles pp " +
                "JOIN users u ON pp.user_id = u.id " +
                "JOIN pablic_profiles_users upp ON pp.id = upp.pablic_profile_id " +
                "WHERE upp.users_id = ?";

        return jdbcTemplate.query(sql, new Object[]{userId}, (ResultSet rs, int rowNum) ->
                GetAllPublicProfileResponse.builder()
                        .id(rs.getLong("id"))
                        .avatar(rs.getString("avatar"))
                        .pablicName(rs.getString("pablic_name"))
                        .tematica(Tematica.valueOf(rs.getString("tematica")))
                        .owner(rs.getString("owner"))
                        .build()
        );
    }
}
