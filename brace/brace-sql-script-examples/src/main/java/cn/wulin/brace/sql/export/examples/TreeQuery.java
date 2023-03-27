package cn.wulin.brace.sql.export.examples;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import cn.hutool.core.util.StrUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TreeQuery {

	private static final String INSERT_TEMPLATE = "INSERT INTO sys_code(ID, CHINESECHAR, CODE, CODENAME, ISDEFAULT, MAPPINGCODE, MEMOVALUE, PXNUM, PARENTID, IS_DEPRECATED) VALUES ('{}', '{}', '{}', '{}', {}, '{}', '{}', {}, '{}', {});";

	private static DataSource dataSource;
	private static JdbcTemplate jdbcTemplate;

	static {
		try {
			Properties properties = new Properties();
			properties.setProperty("driverClassName", "com.mysql.cj.jdbc.Driver");
			properties.setProperty("url",
					"jdbc:mysql://192.168.2.81/ebmp_cas?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8");
			properties.setProperty("username", "ebmp_cas");
			properties.setProperty("password", "123456789");
			properties.setProperty("initialSize", "5");
			properties.setProperty("maxActive", "20");
			properties.setProperty("maxWait", "60000");

			dataSource = DruidDataSourceFactory.createDataSource(properties);
			jdbcTemplate = new JdbcTemplate(dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		List<Map<String, Object>> resultList = queryTree();
		for (Map<String, Object> map : resultList) {
			System.out.println(buildInsertSql(map));
		}
	}

	private static List<Map<String, Object>> queryTree() {
		String sql = "SELECT * FROM sys_code where id='cd6ac24195884f53a28b48747453a21a' ORDER BY PXNUM ASC";
		List<Map<String, Object>> list = jdbcTemplate.query(sql, (ResultSet rs) -> {
			List<Map<String, Object>> result = new ArrayList<>();
			Map<String, Map<String, Object>> map = new HashMap<>();
			while (rs.next()) {
				Map<String, Object> item = new HashMap<>();
				String id = rs.getString("ID");
				item.put("ID", id);
				item.put("CHINESECHAR", rs.getString("CHINESECHAR"));
				item.put("CODE", rs.getString("CODE"));
				item.put("CODENAME", rs.getString("CODENAME"));
				item.put("ISDEFAULT", rs.getInt("ISDEFAULT"));
				item.put("MAPPINGCODE", rs.getString("MAPPINGCODE"));
				item.put("MEMOVALUE", rs.getString("MEMOVALUE"));
				item.put("PXNUM", rs.getInt("PXNUM"));
				item.put("PARENTID", rs.getString("PARENTID"));
				item.put("IS_DEPRECATED", rs.getInt("IS_DEPRECATED"));
				map.put(id, item);
				if (StrUtil.isBlank(rs.getString("PARENTID"))) {
					result.add(item);
				}
			}
			for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
				String parentId = (String) entry.getValue().get("PARENTID");
				if (StrUtil.isNotBlank(parentId)) {
					Map<String, Object> parent = map.get(parentId);
					if (parent != null) {
						List<Map<String, Object>> children = (List<Map<String, Object>>) parent.get("children");
						if (children == null) {
							children = new ArrayList();
							parent.put("children", children);
						}
						children.add(entry.getValue());
					}
				}
			}
			return result;
		});
		return list;
	}

	private static String buildInsertSql(Map<String, Object> map) {
		StringBuilder builder = new StringBuilder();
		builder.append(StrUtil.format(INSERT_TEMPLATE, map.get("ID"), map.get("CHINESECHAR"), map.get("CODE"),
				map.get("CODENAME"), map.get("ISDEFAULT"), map.get("MAPPINGCODE"), map.get("MEMOVALUE"),
				map.get("PXNUM"), map.get("PARENTID"), map.get("IS_DEPRECATED")));
		List<Map<String, Object>> children = (List<Map<String, Object>>) map.get("children");
		if (children != null) {
			for (Map<String, Object> child : children) {
				builder.append(buildInsertSql(child));
			}
		}
		return builder.toString();
	}
}
