package chap11;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


public class MemberDao {
	private JdbcTemplate jdbcTemplate;

	
	public MemberDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	public Member selectByEmail(String email) {
		List<Member> results = jdbcTemplate.query(
				"select * from Member where email = ?"
				, new RowMapper<Member>() {
					@Override
					public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
						Member member = new Member(
								rs.getString("EMAIL"),
								rs.getString("PASSWORD"),
								rs.getString("NAME"),
								rs.getTimestamp("REGDATE").toLocalDateTime());
						
						member.setId(rs.getLong("ID"));
						return member;
	
								
					}
				}
				
				
				
				
				,email);
		
		
		return results.isEmpty() ? null : results.get(0);
				
	}
	
	public void insert(Member member) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pstmt = con.prepareStatement(
						"insert into Member (EMAIL, PASSWORD, NAME, REGDATE)" + "values(?,?,?,?)",
						new String[] {"ID"} );
				pstmt.setString(1, member.getEmail());
				pstmt.setString(2, member.getPassword());
				pstmt.setString(3, member.getName());
				pstmt.setTimestamp(4,
						Timestamp.valueOf(member.getRegisterDateTime()));
						
				return pstmt;
			}
		}, keyHolder);
		
		Number KeyValue = keyHolder.getKey();
		member.setId(KeyValue.longValue());
	}
	
	public void update(Member member) {
		jdbcTemplate.update(
				"update Member set Name = ?, Password = ? where Email = ?",
				member.getName(), member.getPassword(), member.getEmail());
	}
	
	// 결과 행이 1개일때 사용가능
	public int count() {
		Integer count = jdbcTemplate.queryForObject(
				"select count(*) from Member", Integer.class);
		return count;
				
	}
	
	public List<Member> selectAll() {
		List<Member> results = jdbcTemplate.query("select * from Member",
							new RowMapper<Member>() {
					@Override
					public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
						Member member = new Member(
								rs.getString("EMAIL"),
								rs.getString("PASSWORD"),
								rs.getString("NAME"),
								rs.getTimestamp("REGDATE").toLocalDateTime());
						member.setId(rs.getLong("ID"));
						return member;
					}
			
			
		});
		
		return results;
	}
}
